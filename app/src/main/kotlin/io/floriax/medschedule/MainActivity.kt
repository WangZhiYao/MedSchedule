package io.floriax.medschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import io.floriax.medschedule.core.common.extension.logger
import io.floriax.medschedule.shared.designsystem.theme.AppTheme

/**
 *
 *
 * @author WangZhiYao
 * @since 2025/5/30
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val logger by logger<MainActivity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        logger.d("onCreate")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                MedScheduleApp()
            }
        }
    }

    override fun onRestart() {
        logger.d("onRestart")
        super.onRestart()
    }

    override fun onStart() {
        logger.d("onStart")
        super.onStart()
    }

    override fun onResume() {
        logger.d("onResume")
        super.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        logger.d("onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        logger.d("onRestoreInstanceState")
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onPause() {
        logger.d("onPause")
        super.onPause()
    }

    override fun onStop() {
        logger.d("onStop")
        super.onStop()
    }

    override fun onDestroy() {
        logger.d("onDestroy")
        super.onDestroy()
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    AppTheme {
        MedScheduleApp()
    }
}