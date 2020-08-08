package uk.dioxic.mgenerate.cli.metric

import com.mongodb.bulk.BulkWriteResult
import java.time.LocalDateTime
import java.time.Period
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.toKotlinDuration

@ExperimentalTime
data class BulkWriteMetric(
        override val duration: Duration,
        override val operationCount: Long = 1L,
        override val timestamp: LocalDateTime,
        override val elapsedDuration: Duration = duration,
        val insertedCount: Long,
        val deletedCount: Long,
        val matchedCount: Long,
        val modifiedCount: Long) : Metric {

    constructor(result: BulkWriteResult, duration: Duration) : this(
            timestamp = LocalDateTime.now(),
            duration = duration,
            insertedCount = result.insertedCount.toLong(),
            deletedCount = result.deletedCount.toLong(),
            matchedCount = result.matchedCount.toLong(),
            modifiedCount = result.modifiedCount.toLong(),
    )

    override operator fun plus(metric: Metric): BulkWriteMetric {
        if (metric !is BulkWriteMetric) {
            throw UnsupportedOperationException("cannot operate on different metric implementations")
        }

        return BulkWriteMetric(
                timestamp = timestamp.coerceAtLeast(metric.timestamp),
                duration = duration + metric.duration,
                operationCount = operationCount + metric.operationCount,
                insertedCount = insertedCount + metric.insertedCount,
                deletedCount = deletedCount + metric.deletedCount,
                matchedCount = matchedCount + metric.matchedCount,
                modifiedCount = modifiedCount + metric.modifiedCount
        )
    }

    override operator fun minus(metric: Metric): BulkWriteMetric {
        if (metric !is BulkWriteMetric) {
            throw UnsupportedOperationException("cannot operate on different metric implementations")
        }
        return BulkWriteMetric(
                timestamp = timestamp.coerceAtLeast(metric.timestamp),
                duration = duration - metric.duration,
                elapsedDuration = java.time.Duration.between(metric.timestamp, timestamp).toKotlinDuration(),
                operationCount = operationCount - metric.operationCount,
                insertedCount = insertedCount - metric.insertedCount,
                deletedCount = deletedCount - metric.deletedCount,
                matchedCount = matchedCount - metric.matchedCount,
                modifiedCount = modifiedCount - metric.modifiedCount
        )
    }

    override val summaryHeader = listOf("inserts/s", "deletes/s", "matched/s", "modified/s", "operations/s", "latency (ms)")
    override val summary
        get() = arrayOf(rate(insertedCount),
                rate(deletedCount),
                rate(matchedCount),
                rate(modifiedCount),
                rate(operationCount),
                latency()).map { it.toString() }

}