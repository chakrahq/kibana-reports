/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */

package com.amazon.opendistroforelasticsearch.reportsscheduler.settings

import com.amazon.opendistroforelasticsearch.reportsscheduler.ReportsSchedulerPlugin.Companion.LOG_PREFIX
import com.amazon.opendistroforelasticsearch.reportsscheduler.ReportsSchedulerPlugin.Companion.PLUGIN_NAME
import org.apache.logging.log4j.LogManager
import org.elasticsearch.bootstrap.BootstrapInfo
import org.elasticsearch.cluster.service.ClusterService
import org.elasticsearch.common.settings.Setting
import org.elasticsearch.common.settings.Setting.Property.Dynamic
import org.elasticsearch.common.settings.Setting.Property.NodeScope
import org.elasticsearch.common.settings.Settings
import java.io.IOException
import java.nio.file.Path

/**
 * settings specific to reports scheduler Plugin.
 */
internal object PluginSettings {

    /**
     * Settings Key prefix for this plugin.
     */
    private const val KEY_PREFIX = "opendistro.reports.scheduler"

    /**
     * General settings Key prefix.
     */
    private const val GENERAL_KEY_PREFIX = "$KEY_PREFIX.general"

    /**
     * Polling settings Key prefix.
     */
    private const val POLLING_KEY_PREFIX = "$KEY_PREFIX.polling"

    /**
     * Operation timeout for network operations.
     */
    private const val OPERATION_TIMEOUT_MS_KEY = "$GENERAL_KEY_PREFIX.operationTimeoutMs"

    /**
     * Setting to choose Job lock duration.
     */
    private const val JOB_LOCK_DURATION_S_KEY = "$POLLING_KEY_PREFIX.jobLockDurationSeconds"

    /**
     * Setting to choose Minimum polling duration.
     */
    private const val MIN_POLLING_DURATION_S_KEY = "$POLLING_KEY_PREFIX.minPollingDurationSeconds"

    /**
     * Setting to choose Maximum polling duration.
     */
    private const val MAX_POLLING_DURATION_S_KEY = "$POLLING_KEY_PREFIX.maxPollingDurationSeconds"

    /**
     * Setting to choose Maximum number of retries to try locking.
     */
    private const val MAX_LOCK_RETRIES_KEY = "$POLLING_KEY_PREFIX.maxLockRetries"

    /**
     * Default operation timeout for network operations.
     */
    private const val DEFAULT_OPERATION_TIMEOUT_MS = 60000L

    /**
     * Minimum operation timeout for network operations.
     */
    private const val MINIMUM_OPERATION_TIMEOUT_MS = 100L

    /**
     * Default Job lock duration.
     */
    private const val DEFAULT_JOB_LOCK_DURATION_S = 300

    /**
     * Minimum Job lock duration.
     */
    private const val MINIMUM_JOB_LOCK_DURATION_S = 10

    /**
     * Default Minimum polling duration.
     */
    private const val DEFAULT_MIN_POLLING_DURATION_S = 300

    /**
     * Minimum Min polling duration.
     */
    private const val MINIMUM_MIN_POLLING_DURATION_S = 60

    /**
     * Default Maximum polling duration.
     */
    private const val DEFAULT_MAX_POLLING_DURATION_S = 900

    /**
     * Minimum Maximum polling duration.
     */
    private const val MINIMUM_MAX_POLLING_DURATION_S = 300

    /**
     * Default number of retries to try locking.
     */
    private const val DEFAULT_MAX_LOCK_RETRIES = 4

    /**
     * Minimum number of retries to try locking.
     */
    private const val MINIMUM_LOCK_RETRIES = 1

    /**
     * Operation timeout setting in ms for I/O operations
     */
    @Volatile
    var operationTimeoutMs: Long

    /**
     * Job lock duration
     */
    @Volatile
    var jobLockDurationSeconds: Int

    /**
     * Minimum polling duration
     */
    @Volatile
    var minPollingDurationSeconds: Int

    /**
     * Maximum polling duration.
     */
    @Volatile
    var maxPollingDurationSeconds: Int

    /**
     * Max number of retries to try locking.
     */
    @Volatile
    var maxLockRetries: Int

    private const val DECIMAL_RADIX: Int = 10

    private val log = LogManager.getLogger(javaClass)
    private val defaultSettings: Map<String, String>

    init {
        var settings: Settings? = null
        val configDirName = BootstrapInfo.getSystemProperties()?.get("es.path.conf")?.toString()
        if (configDirName != null) {
            val defaultSettingYmlFile = Path.of(configDirName, PLUGIN_NAME, "reports-scheduler.yml")
            try {
                settings = Settings.builder().loadFromPath(defaultSettingYmlFile).build()
            } catch (exception: IOException) {
                log.warn("$LOG_PREFIX:Failed to load ${defaultSettingYmlFile.toAbsolutePath()}")
            }
        }
        // Initialize the settings values to default values
        operationTimeoutMs = (settings?.get(OPERATION_TIMEOUT_MS_KEY)?.toLong()) ?: DEFAULT_OPERATION_TIMEOUT_MS
        jobLockDurationSeconds = (settings?.get(JOB_LOCK_DURATION_S_KEY)?.toInt()) ?: DEFAULT_JOB_LOCK_DURATION_S
        minPollingDurationSeconds = (settings?.get(MIN_POLLING_DURATION_S_KEY)?.toInt())
            ?: DEFAULT_MIN_POLLING_DURATION_S
        maxPollingDurationSeconds = (settings?.get(MAX_POLLING_DURATION_S_KEY)?.toInt())
            ?: DEFAULT_MAX_POLLING_DURATION_S
        maxLockRetries = (settings?.get(MAX_LOCK_RETRIES_KEY)?.toInt()) ?: DEFAULT_MAX_LOCK_RETRIES

        defaultSettings = mapOf(
            OPERATION_TIMEOUT_MS_KEY to operationTimeoutMs.toString(DECIMAL_RADIX),
            JOB_LOCK_DURATION_S_KEY to jobLockDurationSeconds.toString(DECIMAL_RADIX),
            MIN_POLLING_DURATION_S_KEY to minPollingDurationSeconds.toString(DECIMAL_RADIX),
            MAX_POLLING_DURATION_S_KEY to maxPollingDurationSeconds.toString(DECIMAL_RADIX),
            MAX_LOCK_RETRIES_KEY to maxLockRetries.toString(DECIMAL_RADIX)
        )
    }

    private val OPERATION_TIMEOUT_MS: Setting<Long> = Setting.longSetting(
        OPERATION_TIMEOUT_MS_KEY,
        defaultSettings[OPERATION_TIMEOUT_MS_KEY]!!.toLong(),
        MINIMUM_OPERATION_TIMEOUT_MS,
        NodeScope, Dynamic
    )

    private val JOB_LOCK_DURATION_S: Setting<Int> = Setting.intSetting(
        JOB_LOCK_DURATION_S_KEY,
        defaultSettings[JOB_LOCK_DURATION_S_KEY]!!.toInt(),
        MINIMUM_JOB_LOCK_DURATION_S,
        NodeScope, Dynamic
    )

    private val MIN_POLLING_DURATION_S: Setting<Int> = Setting.intSetting(
        MIN_POLLING_DURATION_S_KEY,
        defaultSettings[MIN_POLLING_DURATION_S_KEY]!!.toInt(),
        MINIMUM_MIN_POLLING_DURATION_S,
        NodeScope, Dynamic
    )

    private val MAX_POLLING_DURATION_S: Setting<Int> = Setting.intSetting(
        MAX_POLLING_DURATION_S_KEY,
        defaultSettings[MAX_POLLING_DURATION_S_KEY]!!.toInt(),
        MINIMUM_MAX_POLLING_DURATION_S,
        NodeScope, Dynamic
    )

    private val MAX_LOCK_RETRIES: Setting<Int> = Setting.intSetting(
        MAX_LOCK_RETRIES_KEY,
        defaultSettings[MAX_LOCK_RETRIES_KEY]!!.toInt(),
        MINIMUM_LOCK_RETRIES,
        NodeScope, Dynamic
    )

    /**
     * Returns list of additional settings available specific to this plugin.
     *
     * @return list of settings defined in this plugin
     */
    fun getAllSettings(): List<Setting<*>> {
        return listOf(OPERATION_TIMEOUT_MS,
            JOB_LOCK_DURATION_S,
            MIN_POLLING_DURATION_S,
            MAX_POLLING_DURATION_S,
            MAX_LOCK_RETRIES
        )
    }

    /**
     * Update the setting variables to setting values from local settings
     * @param clusterService cluster service instance
     */
    private fun updateSettingValuesFromLocal(clusterService: ClusterService) {
        operationTimeoutMs = OPERATION_TIMEOUT_MS.get(clusterService.settings)
        jobLockDurationSeconds = JOB_LOCK_DURATION_S.get(clusterService.settings)
        minPollingDurationSeconds = MIN_POLLING_DURATION_S.get(clusterService.settings)
        maxPollingDurationSeconds = MAX_POLLING_DURATION_S.get(clusterService.settings)
        maxLockRetries = MAX_LOCK_RETRIES.get(clusterService.settings)
    }

    /**
     * Update the setting variables to setting values from cluster settings
     * @param clusterService cluster service instance
     */
    private fun updateSettingValuesFromCluster(clusterService: ClusterService) {
        val clusterOperationTimeoutMs = clusterService.clusterSettings.get(OPERATION_TIMEOUT_MS)
        if (clusterOperationTimeoutMs != null) {
            log.debug("$LOG_PREFIX:$OPERATION_TIMEOUT_MS_KEY -autoUpdatedTo-> $clusterOperationTimeoutMs")
            operationTimeoutMs = clusterOperationTimeoutMs
        }
        val clusterJobLockDurationSeconds = clusterService.clusterSettings.get(JOB_LOCK_DURATION_S)
        if (clusterJobLockDurationSeconds != null) {
            log.debug("$LOG_PREFIX:$JOB_LOCK_DURATION_S_KEY -autoUpdatedTo-> $clusterJobLockDurationSeconds")
            jobLockDurationSeconds = clusterJobLockDurationSeconds
        }
        val clusterMinPollingDurationSeconds = clusterService.clusterSettings.get(MIN_POLLING_DURATION_S)
        if (clusterMinPollingDurationSeconds != null) {
            log.debug("$LOG_PREFIX:$MIN_POLLING_DURATION_S_KEY -autoUpdatedTo-> $clusterMinPollingDurationSeconds")
            minPollingDurationSeconds = clusterMinPollingDurationSeconds
        }
        val clusterMaxPollingDurationSeconds = clusterService.clusterSettings.get(MAX_POLLING_DURATION_S)
        if (clusterMaxPollingDurationSeconds != null) {
            log.debug("$LOG_PREFIX:$MAX_POLLING_DURATION_S_KEY -autoUpdatedTo-> $clusterMaxPollingDurationSeconds")
            maxPollingDurationSeconds = clusterMaxPollingDurationSeconds
        }
        val clusterMaxLockRetries = clusterService.clusterSettings.get(MAX_LOCK_RETRIES)
        if (clusterMaxLockRetries != null) {
            log.debug("$LOG_PREFIX:$MAX_LOCK_RETRIES_KEY -autoUpdatedTo-> $clusterMaxLockRetries")
            maxLockRetries = clusterMaxLockRetries
        }
    }

    /**
     * adds Settings update listeners to all settings.
     * @param clusterService cluster service instance
     */
    fun addSettingsUpdateConsumer(clusterService: ClusterService) {
        updateSettingValuesFromLocal(clusterService)
        // Update the variables to cluster setting values
        // If the cluster is not yet started then we get default values again
        updateSettingValuesFromCluster(clusterService)

        clusterService.clusterSettings.addSettingsUpdateConsumer(OPERATION_TIMEOUT_MS) {
            operationTimeoutMs = it
            log.info("$LOG_PREFIX:$OPERATION_TIMEOUT_MS_KEY -updatedTo-> $it")
        }
        clusterService.clusterSettings.addSettingsUpdateConsumer(JOB_LOCK_DURATION_S) {
            jobLockDurationSeconds = it
            log.info("$LOG_PREFIX:$JOB_LOCK_DURATION_S_KEY -updatedTo-> $it")
        }
        clusterService.clusterSettings.addSettingsUpdateConsumer(MIN_POLLING_DURATION_S) {
            minPollingDurationSeconds = it
            log.info("$LOG_PREFIX:$MIN_POLLING_DURATION_S_KEY -updatedTo-> $it")
        }
        clusterService.clusterSettings.addSettingsUpdateConsumer(MAX_POLLING_DURATION_S) {
            maxPollingDurationSeconds = it
            log.info("$LOG_PREFIX:$MAX_POLLING_DURATION_S_KEY -updatedTo-> $it")
        }
        clusterService.clusterSettings.addSettingsUpdateConsumer(MAX_LOCK_RETRIES) {
            maxLockRetries = it
            log.info("$LOG_PREFIX:$MAX_LOCK_RETRIES_KEY -updatedTo-> $it")
        }
    }
}
