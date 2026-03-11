package ci.nsu.mobile.main

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T> Flow<T>.observe(owner: LifecycleOwner, observer: (T) -> Unit) {
    owner.lifecycleScope.launch {
        collect { value ->
            observer(value)
        }
    }
}