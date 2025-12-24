package fox.starter.engine.util

import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

class ObservableDelegate<T, Owner>(
    private var value: T,
    private val changedProperties: MutableSet<KProperty1<Owner, Any?>>
) {

    operator fun getValue(thisRef: Owner, property: KProperty<*>): T {
        return value
    }

    operator fun setValue(thisRef: Owner, property: KProperty<*>, newValue: T) {
        value = newValue
        @Suppress("UNCHECKED_CAST")
        val kProperty1 = property as KProperty1<Owner, Any?>
        changedProperties += kProperty1
    }
}

fun <T, Owner> observable(
    initialValue: T,
    changedProperties: MutableSet<KProperty1<Owner, Any?>>
): ObservableDelegate<T, Owner> = ObservableDelegate(initialValue, changedProperties)