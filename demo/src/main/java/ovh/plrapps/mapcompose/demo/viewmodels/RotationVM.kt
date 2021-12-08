package ovh.plrapps.mapcompose.demo.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ovh.plrapps.mapcompose.api.*
import ovh.plrapps.mapcompose.demo.providers.makeTileStreamProvider
import ovh.plrapps.mapcompose.ui.state.MapState

class RotationVM(application: Application) : AndroidViewModel(application) {
    private val appContext: Context by lazy {
        getApplication<Application>().applicationContext
    }
    private val tileStreamProvider = makeTileStreamProvider(appContext)

    val state: MapState by mutableStateOf(
        MapState(4, 4096, 4096).apply {
            addLayer(tileStreamProvider)
            enableRotation()
            setScrollOffsetRatio(0.3f, 0.3f)
            scale = 0f

            /* Not useful here, just showing how this API works */
            setStateChangeListener {
                println("scale: $scale, scroll: $scroll, rotation: $rotation")
            }
        }
    )

    fun onRotate() {
        viewModelScope.launch {
            state.rotateTo(state.rotation + 90f)
        }
    }
}