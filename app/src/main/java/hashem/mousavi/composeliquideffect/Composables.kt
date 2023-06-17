package hashem.mousavi.composeliquideffect

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.RenderEffect
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen() {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    val progress by animateFloatAsState(
        targetValue = if (isExpanded) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
    )
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ButtonGroup(
            progress = progress,
            renderEffect = generateRenderEffect(),
            toggle = {
                isExpanded = !isExpanded
            }
        )
    }
}

private fun generateRenderEffect(): RenderEffect {
    val renderEffect: RenderEffect
    val blurEffect = BlurEffect(
        radiusX = 50f,
        radiusY = 50f,
        edgeTreatment = TileMode.Mirror
    ).asAndroidRenderEffect()
    val colorMatrix = ColorMatrix(
        floatArrayOf(
            1f, 0f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f, 0f,
            0f, 0f, 1f, 0f, 0f,
            0f, 0f, 0f, 100f, -10000f,
        )
    )

    val matrix = ColorMatrixColorFilter(colorMatrix)
    renderEffect =
        RenderEffect.createChainEffect(RenderEffect.createColorFilterEffect(matrix), blurEffect)
    return renderEffect
}

@Composable
fun AnimatedButton(
    modifier: Modifier,
    imageVector: ImageVector,
    onClick: () -> Unit = {},
) {
    Button(
        modifier = modifier.requiredSize(70.dp),
        onClick = onClick,
        shape = CircleShape
    ) {
        Icon(
            modifier = Modifier
                .fillMaxSize(),
            imageVector = imageVector,
            contentDescription = ""
        )
    }
}

@Composable
fun ButtonGroup(
    progress: Float,
    renderEffect: RenderEffect? = null,
    toggle: () -> Unit = {},
) {
    @Composable
    fun Content() {
        AnimatedButton(
            modifier = Modifier
                .padding(
                    paddingValues = PaddingValues(
                        start = 180.dp,
                        bottom = 100.dp
                    ) * FastOutSlowInEasing
                        .transform((progress))
                )
                .background(Color.Magenta, shape = CircleShape),
            imageVector = Icons.Default.Call,
            onClick = toggle
        )

        AnimatedButton(
            modifier = Modifier
                .padding(
                    paddingValues = PaddingValues(
                        end = 180.dp,
                        bottom = 100.dp
                    ) * FastOutSlowInEasing.transform(progress)
                )
                .background(Color.Magenta, shape = CircleShape),
            imageVector = Icons.Default.Email,
            onClick = toggle
        )

        AnimatedButton(
            modifier = Modifier
                .padding(
                    paddingValues = PaddingValues(
                        end = 180.dp,
                        top = 100.dp
                    ) * FastOutSlowInEasing.transform(progress)
                )
                .background(Color.Magenta, shape = CircleShape),
            imageVector = Icons.Default.Delete,
            onClick = toggle
        )

        AnimatedButton(
            modifier = Modifier
                .padding(
                    paddingValues = PaddingValues(
                        start = 180.dp,
                        top = 100.dp
                    ) * FastOutSlowInEasing.transform(progress)
                )
                .background(Color.Magenta, shape = CircleShape),
            imageVector = Icons.Default.Info,
            onClick = toggle
        )

        AnimatedButton(
            modifier = Modifier
                .background(Color.Magenta, shape = CircleShape)
                .rotate(degrees = 5 * 45 * FastOutSlowInEasing.transform(progress)),
            imageVector = Icons.Default.Add,
            onClick = toggle
        )
    }
    Box {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { this.renderEffect = renderEffect?.asComposeRenderEffect() },
            contentAlignment = Alignment.Center
        ) {
            Content()
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Content()
        }
    }

}

private operator fun PaddingValues.times(factor: Float): PaddingValues {
    return PaddingValues(
        start = this.calculateStartPadding(LayoutDirection.Ltr) * factor,
        end = this.calculateEndPadding(LayoutDirection.Ltr) * factor,
        top = this.calculateTopPadding() * factor,
        bottom = this.calculateBottomPadding() * factor
    )
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}
