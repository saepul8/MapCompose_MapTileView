package ovh.plrapps.mapcompose.demo.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ovh.plrapps.mapcompose.api.*
import ovh.plrapps.mapcompose.core.TileStreamProvider
import ovh.plrapps.mapcompose.ui.state.MapState

class LayersVM (application: Application) : AndroidViewModel(application) {
    private val appContext: Context by lazy {
        getApplication<Application>().applicationContext
    }
    private val tileStreamProvider = makeTileStreamProvider(appContext, "mont_blanc")
    private val satelliteProvider = makeTileStreamProvider(appContext, "mont_blanc_satellite")
    private val ignV2Provider = makeTileStreamProvider(appContext, "mont_blanc_ignv2")

    private var satelliteId: String? = null
    private var ignV2Id: String? = null

    val state: MapState by mutableStateOf(
        MapState(4, 4096, 4096).apply {
            shouldLoopScale = true
            enableRotation()
            viewModelScope.launch {
                scrollTo(0.5, 0.5, 1f)
            }

            addLayer(tileStreamProvider)
            satelliteId = addLayer(satelliteProvider)
            ignV2Id = addLayer(ignV2Provider, 0.5f)
        }
    )

    private fun makeTileStreamProvider(appContext: Context, folder: String) =
        TileStreamProvider { row, col, zoomLvl ->
            try {
                appContext.assets?.open("tiles/$folder/$zoomLvl/$row/$col.jpg")
            } catch (e: Exception) {
                null
            }
        }

    fun setSatelliteOpacity(opacity: Float) {
        satelliteId?.also { id ->
            state.setLayerOpacity(id, opacity)
        }

    }

    fun setIgnV2Opacity(opacity: Float) {
        ignV2Id?.also { id ->
            state.setLayerOpacity(id, opacity)
        }
    }
}