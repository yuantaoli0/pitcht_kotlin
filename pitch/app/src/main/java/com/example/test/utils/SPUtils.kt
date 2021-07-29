package com.example.test.utils

import android.content.Context
import android.content.SharedPreferences.Editor
import android.util.Base64
import java.io.*
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * className: SPUtils
 * description:
 * author: hong
 * datetime: 2016/10/28 10:01
 */
object SPUtils {
    const val FILE_NAME = "pith"
    fun putBean(context: Context, key: String?, obj: Any?) {
        putBean(context, FILE_NAME, key, obj)
    }

    fun putBean(context: Context, fileName: String?, key: String?, obj: Any?) {
        if (obj is Serializable) { // obj必须实现Serializable接口，否则会出问题
            try {
                val baos = ByteArrayOutputStream()
                val oos = ObjectOutputStream(baos)
                oos.writeObject(obj)
                val string64 = String(Base64.encode(baos.toByteArray(), 0))
                val sp =
                    context.applicationContext.getSharedPreferences(fileName, Context.MODE_PRIVATE)
                val editor = sp.edit()
                editor.putString(key, string64)
                SharedPreferencesCompat.apply(editor)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            throw IllegalArgumentException(
                "the obj must implement Serializble")
        }
    }

    fun getBean(context: Context, key: String?): Any? {
        return getBean(context, FILE_NAME, key)
    }

    fun getBean(context: Context, fileName: String?, key: String?): Any? {
        var obj: Any? = null
        try {
            val base64 =
                context.applicationContext.getSharedPreferences(fileName, Context.MODE_PRIVATE)
                    .getString(key, "")
            if (base64 == "") {
                return null
            }
            val base64Bytes = Base64.decode(base64!!.toByteArray(), 1)
            val bais = ByteArrayInputStream(base64Bytes)
            val ois = ObjectInputStream(bais)
            obj = ois.readObject()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return obj
    }

    fun put(context: Context, key: String?, `object`: Any) {
        put(context, FILE_NAME, key, `object`)
    }

    fun put(context: Context, fileName: String?, key: String?, `object`: Any) {
        val sp = context.applicationContext.getSharedPreferences(fileName,
            Context.MODE_PRIVATE)
        val editor = sp.edit()
        if (`object` is String) {
            editor.putString(key, `object`)
        } else if (`object` is Int) {
            editor.putInt(key, `object`)
        } else if (`object` is Boolean) {
            editor.putBoolean(key, `object`)
        } else if (`object` is Float) {
            editor.putFloat(key, `object`)
        } else if (`object` is Long) {
            editor.putLong(key, `object`)
        } else {
            editor.putString(key, `object`.toString())
        }
        SharedPreferencesCompat.apply(editor)
    }

    operator fun <T> get(context: Context, key: String?, t: T): T? {
        return SPUtils[context.applicationContext, FILE_NAME, key, t] as T?
    }

    operator fun get(context: Context, fileName: String?, key: String?, defaultObject: Any?): Any? {
        val sp = context.applicationContext.getSharedPreferences(fileName,
            Context.MODE_PRIVATE)
        if (defaultObject is String) {
            return sp.getString(key, defaultObject as String?)
        } else if (defaultObject is Int) {
            return sp.getInt(key, (defaultObject as Int?)!!)
        } else if (defaultObject is Boolean) {
            return sp.getBoolean(key, (defaultObject as Boolean?)!!)
        } else if (defaultObject is Float) {
            return sp.getFloat(key, (defaultObject as Float?)!!)
        } else if (defaultObject is Long) {
            return sp.getLong(key, (defaultObject as Long?)!!)
        }
        return null
    }

    fun remove(context: Context, key: String?) {
        val sp = context.applicationContext.getSharedPreferences(FILE_NAME,
            Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.remove(key)
        SharedPreferencesCompat.apply(editor)
    }

    fun clear(context: Context) {
        val sp = context.applicationContext.getSharedPreferences(FILE_NAME,
            Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.clear()
        SharedPreferencesCompat.apply(editor)
    }

    fun contains(context: Context, key: String?): Boolean {
        val sp = context.applicationContext.getSharedPreferences(FILE_NAME,
            Context.MODE_PRIVATE)
        return sp.contains(key)
    }

    fun getAll(context: Context): Map<String, *> {
        val sp = context.applicationContext.getSharedPreferences(FILE_NAME,
            Context.MODE_PRIVATE)
        return sp.all
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private object SharedPreferencesCompat {
        private val sApplyMethod = findApplyMethod()

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        private fun findApplyMethod(): Method? {
            try {
                val clz: Class<*> = Editor::class.java
                return clz.getMethod("apply")
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            }
            return null
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        fun apply(editor: Editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor)
                    return
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
            editor.commit()
        }
    }
}