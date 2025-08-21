/*
 * Copyright 2010-2025 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.internal.diagnostics

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidExtension
import org.jetbrains.kotlin.gradle.plugin.AndroidGradlePluginVersion
import org.jetbrains.kotlin.gradle.plugin.diagnostics.KotlinToolingDiagnostics
import org.jetbrains.kotlin.gradle.plugin.diagnostics.reportDiagnostic
import org.jetbrains.kotlin.gradle.utils.findByType

internal object AgpWithBuiltInKotlinAppliedCheck {
    val minimalBuiltInKotlinSupportedAgpVersion = AndroidGradlePluginVersion(9, 0, 0, "alpha01")

    fun Project.runAgpWithBuiltInKotlinIfAppliedCheck(
        agpVersionProvider: AndroidGradlePluginVersionProvider = AndroidGradlePluginVersionProvider.Default
    ) {
        val isKotlinAndroidExtensionExists = extensions.findByType<KotlinAndroidExtension>() != null
        val agpVersion = agpVersionProvider.get()
        if (isKotlinAndroidExtensionExists &&
            agpVersion != null &&
            agpVersion >= minimalBuiltInKotlinSupportedAgpVersion
        ) {
            project.reportDiagnostic(
                KotlinToolingDiagnostics.AgpWithBuiltInKotlinIsAlreadyApplied(
                    project.buildFile.relativeTo(project.rootDir)
                )
            )
        }
    }

    interface AndroidGradlePluginVersionProvider {
        fun get(): AndroidGradlePluginVersion?

        object Default : AndroidGradlePluginVersionProvider {
            override fun get(): AndroidGradlePluginVersion? {
                return AndroidGradlePluginVersion.currentOrNull
            }
        }
    }
}
