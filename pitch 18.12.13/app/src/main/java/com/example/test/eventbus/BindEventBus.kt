package com.rczs.gis.eventbus

/**
 * @Description:  自定义注释 用于绑定eventBus
 * @CreateDate: 2021/3/25
 * @Author: LSX
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class BindEventBus 