package uk.dioxic.mgenerate.cli.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.output.CliktHelpFormatter
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.groups.cooccurring
import com.github.ajalt.clikt.parameters.groups.provideDelegate
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.long
import com.github.ajalt.clikt.parameters.types.path
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import com.mongodb.client.model.BulkWriteOptions
import com.mongodb.client.model.UpdateOneModel
import com.mongodb.client.model.UpdateOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import uk.dioxic.mgenerate.cli.extension.applyTemplateCodecRegistry
import uk.dioxic.mgenerate.cli.extension.templateOf
import uk.dioxic.mgenerate.cli.options.*
import uk.dioxic.mgenerate.cli.runner.BatchRunner
import uk.dioxic.mgenerate.core.Template
import uk.dioxic.mgenerate.core.VariableCache
import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime

class Update : CliktCommand(help = "Update data in MongoDB") {
    init {
        context { helpFormatter = CliktHelpFormatter(showDefaultValues = true) }
    }

    private val authOptions by AuthOptions().cooccurring()
    private val connOptions by ConnectionOptions()
    private val namespaceOptions by NamespaceOptions()
    private val number by option("-n", "--number", help = "number of documents to update").long().default(1)
    private val batchSize by option("-b", "--batchsize", help = "number of operations to batch together").int().default(100)
    private val parallelism by option(help = "parallelism of write operations").int().default(4)
    private val drop by option(help = "drop collection before load").flag()
    private val ordered by option(help = "enable ordered writes").flag()
    private val upsert by option(help = "enable upsert for updates").flag()
    private val variables by option("-v", "--variables", help = "input variables file").path(mustExist = true, canBeDir = false)
    private val filterTemplate by argument("FILTER").convert { templateOf(it) }
    private val updateTemplate by argument("UPDATE").convert { templateOf(it) }

    @FlowPreview
    @ExperimentalTime
    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    override fun run() {
        if (variables != null) {
            println("Loading variables file...")
            VariableCache.loadCache(variables)
            println("Variables loaded")
        }

        val client = MongoClients.create(MongoClientSettings.builder()
                .applyAuthOptions(authOptions)
                .applyConnectionOptions(connOptions)
                .applyTemplateCodecRegistry()
                .build()
        )

        val collection = client
                .getDatabase(namespaceOptions)
                .getCollection(namespaceOptions, Template::class.java)

        if (drop) collection.drop()

        val bulkWriteOptions = BulkWriteOptions().ordered(ordered)
        val updateOptions = UpdateOptions().upsert(upsert)
        val batchSizeActual = if (variables != null) 1 else batchSize

        val duration = BatchRunner(
                count = number,
                parallelism = parallelism,
                batchSize = batchSizeActual,
                producer = {
                    UpdateOneModel<Template>(
                            filterTemplate,
                            updateTemplate,
                            updateOptions
                    )
                },
                consumer = { collection.bulkWrite(it, bulkWriteOptions) }
        ).call()

        println("Completed in $duration (${(number / duration.inSeconds).roundToInt()} updates/s)")

    }

}
