package com.jetbrains.kmpapp

import com.jetbrains.kmpapp.core.PlatformConfig
import dev.zacsweers.metro.createGraphFactory
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@OptIn(ExperimentalAtomicApi::class)
object Init {

    private val graphRef = AtomicReference<AppGraph?>(null)

    val appGraph: AppGraph
        get() = checkNotNull(graphRef.load()) { "Init.initAll() must run before the graph is accessed" }

    fun initAll(isDebug: Boolean) = initAll(PlatformConfig(isDebug = isDebug))

    fun initAll(platformConfig: PlatformConfig) {
        if (graphRef.load() != null) return
        val graph = createGraphFactory<AppGraph.Factory>().create(platformConfig)
        if (!graphRef.compareAndSet(null, graph)) return

        graph.initializers.sortedBy { it.order }.forEach { it.init() }
        graph.observers.forEach { it.start() }
    }
}
