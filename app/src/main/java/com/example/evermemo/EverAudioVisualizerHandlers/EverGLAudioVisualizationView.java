package com.example.evermemo.EverAudioVisualizerHandlers;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.evermemo.MainActivity;
import com.example.evermemo.R;

/**
 * Audio visualization view implementation for OpenGL.
 */
public class EverGLAudioVisualizationView extends GLSurfaceView implements EverAudioVisualization, EverInnerAudioVisualization {

    private static final int EGL_VERSION = 2;
    @NonNull
    private final Configuration configuration;
    public EverGLRenderer renderer;
    public EverGLAudioVisualizationView.Builder builder;
    @Nullable
    private EverDbmHandler<?> dbmHandler;
    @Nullable
    private CalmDownListener innerCalmDownListener;

    private EverGLAudioVisualizationView(@NonNull EverGLAudioVisualizationView.Builder builder) {
        super(builder.context);
        this.builder = builder;
        configuration = new EverGLAudioVisualizationView.Configuration(builder);
        renderer = new EverGLRenderer(getContext(), configuration);
        init();
    }

    public EverGLAudioVisualizationView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        configuration = new EverGLAudioVisualizationView.Configuration(context, attrs, isInEditMode());
        renderer = new EverGLRenderer(getContext(), configuration);
        init();
    }

    public void updateConfigs(@NonNull EverGLAudioVisualizationView.Builder builder) {
        renderer.updateConfiguration(builder);
    }

    private void init() {
        setEGLContextClientVersion(EGL_VERSION);
        setRenderer(renderer);
        renderer.calmDownListener(new CalmDownListener() {
            @Override
            public void onCalmedDown() {
                stopRendering();
                if (innerCalmDownListener != null) {
                    innerCalmDownListener.onCalmedDown();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dbmHandler != null) {
            dbmHandler.onResume();
        }
    }

    @Override
    public void onPause() {
        if (dbmHandler != null) {
            dbmHandler.onPause();
        }
        super.onPause();
    }

    @Override
    public <T> void linkTo(@NonNull EverDbmHandler<T> dbmHandler) {
        if (this.dbmHandler != null) {
            this.dbmHandler.release();
        }
        this.dbmHandler = dbmHandler;
        this.dbmHandler.setUp(this, configuration.layersCount);
    }

    @Override
    public void release() {
        if (dbmHandler != null) {
            dbmHandler.release();
            dbmHandler = null;
        }
    }

    @Override
    public void startRendering() {
        if (getRenderMode() != RENDERMODE_CONTINUOUSLY) {
            setRenderMode(RENDERMODE_CONTINUOUSLY);
        }
    }

    @Override
    public void stopRendering() {
        if (getRenderMode() != RENDERMODE_WHEN_DIRTY) {
            setRenderMode(RENDERMODE_WHEN_DIRTY);
        }
    }

    @Override
    public void calmDownListener(@Nullable CalmDownListener calmDownListener) {
        innerCalmDownListener = calmDownListener;
    }

    @Override
    public void onDataReceived(float[] dBmArray, float[] ampsArray) {
        renderer.onDataReceived(dBmArray, ampsArray);
    }

    public void updateColor(@NonNull EverGLAudioVisualizationView.ColorsBuilder builder) {
        renderer.updateConfiguration(builder);
    }

    /**
     * Audio Visualization renderer interface that allows to change waves' colors at runtime.
     */
    public interface AudioVisualizationRenderer extends Renderer {

        /**
         * Update colors configuration.
         *
         * @param builder instance of color builder.
         */
        void updateConfiguration(@NonNull EverGLAudioVisualizationView.ColorsBuilder builder);
    }

    /**
     * Configuration holder class.
     */
    static class Configuration {

        int wavesCount;
        int layersCount;
        int bubblesPerLayer;
        float bubbleSize;
        float waveHeight;
        float footerHeight;
        boolean randomizeBubbleSize;
        float[] backgroundColor;
        float[][] layerColors;

        public Configuration(@NonNull Context context, AttributeSet attrs, boolean isInEditMode) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GLAudioVisualizationView);
            int[] colors;
            int bgColor;
            try {
                layersCount = array.getInt(R.styleable.GLAudioVisualizationView_av_layersCount, EverConstants.DEFAULT_LAYERS_COUNT);
                layersCount = EverAudioUtils.between(layersCount, EverConstants.MIN_LAYERS_COUNT, EverConstants.MAX_LAYERS_COUNT);
                wavesCount = array.getInt(R.styleable.GLAudioVisualizationView_av_wavesCount, EverConstants.DEFAULT_WAVES_COUNT);
                wavesCount = EverAudioUtils.between(wavesCount, EverConstants.MIN_WAVES_COUNT, EverConstants.MAX_WAVES_COUNT);
                waveHeight = array.getDimensionPixelSize(R.styleable.GLAudioVisualizationView_av_wavesHeight, (int) EverConstants.DEFAULT_WAVE_HEIGHT);
                waveHeight = EverAudioUtils.between(waveHeight, EverConstants.MIN_WAVE_HEIGHT, EverConstants.MAX_WAVE_HEIGHT);
                bubbleSize = array.getDimensionPixelSize(R.styleable.GLAudioVisualizationView_av_bubblesSize, EverConstants.DEFAULT_BUBBLE_SIZE);
                bubbleSize = EverAudioUtils.between(bubbleSize, EverConstants.MIN_BUBBLE_SIZE, EverConstants.MAX_BUBBLE_SIZE);
                randomizeBubbleSize = array.getBoolean(R.styleable.GLAudioVisualizationView_av_bubblesRandomizeSizes, false);
                footerHeight = array.getDimensionPixelSize(R.styleable.GLAudioVisualizationView_av_wavesFooterHeight, (int) EverConstants.DEFAULT_FOOTER_HEIGHT);
                footerHeight = EverAudioUtils.between(footerHeight, EverConstants.MIN_FOOTER_HEIGHT, EverConstants.MAX_FOOTER_HEIGHT);
                bubblesPerLayer = array.getInt(R.styleable.GLAudioVisualizationView_av_bubblesPerLayer, EverConstants.DEFAULT_BUBBLES_PER_LAYER);
                bubblesPerLayer = EverAudioUtils.between(bubblesPerLayer, EverConstants.DEFAULT_BUBBLES_PER_LAYER_MIN, EverConstants.DEFAULT_BUBBLES_PER_LAYER_MAX);
                bgColor = array.getColor(R.styleable.GLAudioVisualizationView_av_backgroundColor, Color.TRANSPARENT);
                if (bgColor == Color.TRANSPARENT) {
                    bgColor = ContextCompat.getColor(context, R.color.av_color_bg);
                }
                int arrayId = array.getResourceId(R.styleable.GLAudioVisualizationView_av_wavesColors, R.array.av_colors);
                if (isInEditMode) {
                    colors = new int[layersCount];
                } else {

                    TypedArray colorsArray = array.getResources().obtainTypedArray(arrayId);
                    //   colors = new int[colorsArray.length()];
                    //   for (int i = 0; i < colorsArray.length(); i++) {
                    //       colors[i] = colorsArray.getColor(i, Color.TRANSPARENT);
                    //   }
                    colors = new int[4];
                    for (int i = 0; i < colorsArray.length(); i++) {
                        if (((MainActivity) unwrap(context)).getActualNote() != null) {
                            colors[i] = Integer.parseInt(((MainActivity) unwrap(context)).getActualNote().getNoteColor());
                        } else {
                            colors[i] = bgColor;
                        }


                    }
                    colorsArray.recycle();
                }
            } finally {
                array.recycle();
            }
            if (colors.length < layersCount) {
                throw new IllegalArgumentException("You specified more layers than colors.");
            }

            layerColors = new float[colors.length][];
            for (int i = 0; i < colors.length; i++) {
                layerColors[i] = EverAudioUtils.convertColor(colors[i]);
            }
            backgroundColor = EverAudioUtils.convertColor(bgColor);
            bubbleSize /= context.getResources().getDisplayMetrics().widthPixels;
        }

        private Configuration(@NonNull EverGLAudioVisualizationView.Builder builder) {
            this.waveHeight = builder.waveHeight;
            waveHeight = EverAudioUtils.between(waveHeight, EverConstants.MIN_WAVE_HEIGHT, EverConstants.MAX_WAVE_HEIGHT);
            this.wavesCount = builder.wavesCount;
            wavesCount = EverAudioUtils.between(wavesCount, EverConstants.MIN_WAVES_COUNT, EverConstants.MAX_WAVES_COUNT);
            this.layerColors = builder.layerColors();
            this.bubbleSize = builder.bubbleSize;
            bubbleSize = EverAudioUtils.between(bubbleSize, EverConstants.MIN_BUBBLE_SIZE, EverConstants.MAX_BUBBLE_SIZE);
            this.bubbleSize = this.bubbleSize / builder.context.getResources().getDisplayMetrics().widthPixels;
            this.footerHeight = builder.footerHeight;
            footerHeight = EverAudioUtils.between(footerHeight, EverConstants.MIN_FOOTER_HEIGHT, EverConstants.MAX_FOOTER_HEIGHT);
            this.randomizeBubbleSize = builder.randomizeBubbleSize;
            this.backgroundColor = builder.backgroundColor();
            this.layersCount = builder.layersCount;
            this.bubblesPerLayer = builder.bubblesPerLayer;
            EverAudioUtils.between(bubblesPerLayer, EverConstants.DEFAULT_BUBBLES_PER_LAYER_MIN, EverConstants.DEFAULT_BUBBLES_PER_LAYER_MAX);
            layersCount = EverAudioUtils.between(layersCount, EverConstants.MIN_LAYERS_COUNT, EverConstants.MAX_LAYERS_COUNT);
            if (layerColors.length < layersCount) {
                throw new IllegalArgumentException("You specified more layers than colors.");
            }
        }

        private static Activity unwrap(Context context) {
            while (!(context instanceof Activity) && context instanceof ContextWrapper) {
                context = ((ContextWrapper) context).getBaseContext();
            }

            return (Activity) context;
        }
    }

    public static class ColorsBuilder<T extends EverGLAudioVisualizationView.ColorsBuilder> {
        @NonNull
        private final Context context;
        private float[] backgroundColor;
        private float[][] layerColors;

        public ColorsBuilder(@NonNull Context context) {
            this.context = context;
        }

        float[][] layerColors() {
            return layerColors;
        }

        float[] backgroundColor() {
            return backgroundColor;
        }

        /**
         * Set background color
         *
         * @param backgroundColor background color
         */
        @NonNull
        public T setBackgroundColor(int backgroundColor) {
            this.backgroundColor = EverAudioUtils.convertColor(backgroundColor);
            return getThis();
        }

        /**
         * Set layer colors from array resource
         *
         * @param arrayId array resource
         */
        @NonNull
        public T setLayerColors(int arrayId) {
            TypedArray colorsArray = context.getResources().obtainTypedArray(arrayId);
            int[] colors = new int[colorsArray.length()];
            for (int i = 0; i < colorsArray.length(); i++) {
                colors[i] = colorsArray.getColor(i, Color.TRANSPARENT);
            }
            colorsArray.recycle();
            return setLayerColors(colors);
        }

        /**
         * Set layer colors.
         *
         * @param colors array of colors
         */
        @NonNull
        public T setLayerColors(@NonNull int[] colors) {
            layerColors = new float[colors.length][];
            for (int i = 0; i < colors.length; i++) {
                layerColors[i] = EverAudioUtils.convertColor(colors[i]);
            }
            return getThis();
        }

        /**
         * Set background color from color resource
         *
         * @param backgroundColor color resource
         */
        @NonNull
        public T setBackgroundColorRes(int backgroundColor) {
            return setBackgroundColor(ContextCompat.getColor(context, backgroundColor));
        }

        @NonNull
        protected T getThis() {
            //noinspection unchecked
            return (T) this;
        }
    }

    public static class Builder extends EverGLAudioVisualizationView.ColorsBuilder<EverGLAudioVisualizationView.Builder> {

        @NonNull
        private final Context context;
        private int wavesCount;
        private int layersCount;
        private float bubbleSize;
        private float waveHeight;
        private float footerHeight;
        private boolean randomizeBubbleSize;
        private int bubblesPerLayer;
        private boolean isTop = false;

        public Builder(@NonNull Context context) {
            super(context);
            this.context = context;
        }

        @NonNull
        @Override
        protected EverGLAudioVisualizationView.Builder getThis() {
            return this;
        }

        /**
         * Set waves count
         *
         * @param wavesCount waves count
         */
        @NonNull
        public EverGLAudioVisualizationView.Builder setWavesCount(int wavesCount) {
            this.wavesCount = wavesCount;
            return this;
        }

        /**
         * Set layers count
         *
         * @param layersCount layers count
         */
        @NonNull
        public EverGLAudioVisualizationView.Builder setLayersCount(int layersCount) {
            this.layersCount = layersCount;
            return this;
        }

        @NonNull
        public EverGLAudioVisualizationView.Builder setIsTop(boolean isTop) {
            this.isTop = isTop;
            return this;
        }

        /**
         * Set bubbles size in pixels
         *
         * @param bubbleSize bubbles size in pixels
         */
        @NonNull
        public EverGLAudioVisualizationView.Builder setBubblesSize(float bubbleSize) {
            this.bubbleSize = bubbleSize;
            return this;
        }

        /**
         * Set bubble size from dimension resource
         *
         * @param bubbleSize dimension resource
         */
        public EverGLAudioVisualizationView.Builder setBubblesSize(@DimenRes int bubbleSize) {
            return setBubblesSize((float) context.getResources().getDimensionPixelSize(bubbleSize));
        }

        /**
         * Set wave height in pixels
         *
         * @param waveHeight wave height in pixels
         */
        @NonNull
        public EverGLAudioVisualizationView.Builder setWavesHeight(float waveHeight) {
            this.waveHeight = waveHeight;
            return this;
        }

        /**
         * Set wave height from dimension resource
         *
         * @param waveHeight dimension resource
         */
        @NonNull
        public EverGLAudioVisualizationView.Builder setWavesHeight(int waveHeight) {
            return setWavesHeight((float) context.getResources().getDimensionPixelSize(waveHeight));
        }

        /**
         * Set footer height in pixels
         *
         * @param footerHeight footer height in pixels
         */
        @NonNull
        public EverGLAudioVisualizationView.Builder setWavesFooterHeight(float footerHeight) {
            this.footerHeight = footerHeight;
            return this;
        }

        /**
         * Set footer height from dimension resource
         *
         * @param footerHeight dimension resource
         */
        @NonNull
        public EverGLAudioVisualizationView.Builder setWavesFooterHeight(int footerHeight) {
            return setWavesFooterHeight((float) context.getResources().getDimensionPixelSize(footerHeight));
        }

        /**
         * Set flag indicates that size of bubbles should be randomized
         *
         * @param randomizeBubbleSize true if size of bubbles should be randomized, false if size of bubbles must be the same
         */
        @NonNull
        public EverGLAudioVisualizationView.Builder setBubblesRandomizeSize(boolean randomizeBubbleSize) {
            this.randomizeBubbleSize = randomizeBubbleSize;
            return this;
        }

        /**
         * Set number of bubbles per layer.
         *
         * @param bubblesPerLayer number of bubbles per layer
         */
        @NonNull
        public EverGLAudioVisualizationView.Builder setBubblesPerLayer(int bubblesPerLayer) {
            this.bubblesPerLayer = bubblesPerLayer;
            return this;
        }

        @NonNull
        public EverGLAudioVisualizationView build() {
            return new EverGLAudioVisualizationView(this);
        }
    }

    /**
     * Renderer builder.
     */
    public static class RendererBuilder {

        @NonNull
        private final EverGLAudioVisualizationView.Builder builder;
        private GLSurfaceView glSurfaceView;
        private EverDbmHandler handler;

        /**
         * Create new renderer using existing Audio Visualization builder.
         *
         * @param builder instance of Audio Visualization builder
         */
        public RendererBuilder(@NonNull EverGLAudioVisualizationView.Builder builder) {
            this.builder = builder;
        }

        /**
         * Set dBm handler.
         *
         * @param handler instance of dBm handler
         */
        @NonNull
        public EverGLAudioVisualizationView.RendererBuilder handler(EverDbmHandler handler) {
            this.handler = handler;
            return this;
        }

        /**
         * Set OpenGL surface view.
         *
         * @param glSurfaceView instance of OpenGL surface view
         */
        @NonNull
        public EverGLAudioVisualizationView.RendererBuilder glSurfaceView(@NonNull GLSurfaceView glSurfaceView) {
            this.glSurfaceView = glSurfaceView;
            this.glSurfaceView.setZOrderOnTop(builder.isTop);
            return this;
        }

        /**
         * Create new Audio Visualization Renderer.
         *
         * @return new Audio Visualization Renderer
         */
        //TODO FINISH THIS
        @NonNull
        public EverGLAudioVisualizationView.AudioVisualizationRenderer build() {
            final EverGLRenderer renderer = new EverGLRenderer(builder.context, new EverGLAudioVisualizationView.Configuration(builder));
            final EverInnerAudioVisualization audioVisualization = new EverInnerAudioVisualization() {
                @Override
                public void startRendering() {
                    if (glSurfaceView.getRenderMode() != RENDERMODE_CONTINUOUSLY) {
                        glSurfaceView.setRenderMode(RENDERMODE_CONTINUOUSLY);
                    }
                }

                @Override
                public void stopRendering() {
                    if (glSurfaceView.getRenderMode() != RENDERMODE_WHEN_DIRTY) {
                        glSurfaceView.setRenderMode(RENDERMODE_WHEN_DIRTY);
                    }
                }

                @Override
                public void calmDownListener(@Nullable CalmDownListener calmDownListener) {

                }

                @Override
                public void onDataReceived(float[] dBmArray, float[] ampsArray) {
                    renderer.onDataReceived(dBmArray, ampsArray);
                }
            };
            renderer.calmDownListener(new CalmDownListener() {
                @Override
                public void onCalmedDown() {
                    audioVisualization.stopRendering();
                }
            });
            handler.setUp(audioVisualization, builder.layersCount);
            return renderer;
        }
    }
}

