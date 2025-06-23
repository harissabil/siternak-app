package com.siternak.app.di

import com.siternak.app.data.ml.PMKClassifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mlModule = module {
    // 1. Classifier untuk Gusi (Mulut)
    single(named("gusiClassifier")) {
        PMKClassifier(get()).apply {
            loadModel("pmk_gusi_yolov8_model.tflite")
        }
    }

    // 2. Classifier untuk Lidah
    single(named("lidahClassifier")) {
        PMKClassifier(get()).apply {
            loadModel("pmk_lidah_simple.tflite")
        }
    }

    // 3. Classifier untuk Air Liur
    single(named("airLiurClassifier")) {
        PMKClassifier(get()).apply {
            loadModel("pmk_air_liur_yolo11_model.tflite")
        }
    }

    // 4. Classifier untuk Kaki
    single(named("kakiClassifier")) {
        PMKClassifier(get()).apply {
            loadModel("pmk_kaki_yolo11_model.tflite")
        }
    }
}