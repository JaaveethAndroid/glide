package com.bumptech.glide.request.target;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class DrawableImageViewTargetTest {

    @Test
    public void testSetsDrawableOnViewInSetResource() {
        ImageView view = new ImageView(Robolectric.application);
        DrawableImageViewTarget target = new DrawableImageViewTarget(view);
        GlideDrawable expected = new MockAnimatedDrawable();

        target.setResource(expected);

        assertEquals(expected, view.getDrawable());
    }

    @Test
    public void testWrapsDrawableInSquaringDrawableIfDrawableAndViewAreSquare() {
        ImageView mockView = mock(ImageView.class);
        when(mockView.getWidth()).thenReturn(100);
        when(mockView.getHeight()).thenReturn(100);
        DrawableImageViewTarget target = new DrawableImageViewTarget(mockView);
        GlideDrawable drawable = new MockAnimatedDrawable() {
            @Override
            public int getIntrinsicHeight() {
                return 100;
            }

            @Override
            public int getIntrinsicWidth() {
                return 100;
            }
        };


        target.onResourceReady(drawable, null);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Drawable drawable = (Drawable) invocation.getArguments()[0];
                assertTrue(drawable instanceof SquaringDrawable);
                return null;
            }
        }).when(mockView).setImageDrawable(any(Drawable.class));
        verify(mockView).setImageDrawable(any(BitmapDrawable.class));
    }

    @Test
    public void testDoesNotWrapInSquaringDrawableIfDrawableIsAnimated() {
        ImageView mockView = mock(ImageView.class);
        when(mockView.getWidth()).thenReturn(100);
        when(mockView.getHeight()).thenReturn(100);
        DrawableImageViewTarget target = new DrawableImageViewTarget(mockView);
        GlideDrawable drawable = new MockAnimatedDrawable() {
            @Override
            public int getIntrinsicHeight() {
                return 100;
            }

            @Override
            public int getIntrinsicWidth() {
                return 100;
            }
        };
         target.onResourceReady(drawable, null);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Drawable drawable = (Drawable) invocation.getArguments()[0];
                assertFalse(drawable instanceof SquaringDrawable);
                return null;
            }
        }).when(mockView).setImageDrawable(any(Drawable.class));
        verify(mockView).setImageDrawable(any(BitmapDrawable.class));
    }

    @Test
    public void testDoesNotWrapInSquaringDrawableIfDrawableIsNotSquare() {
        ImageView mockView = mock(ImageView.class);
        when(mockView.getWidth()).thenReturn(100);
        when(mockView.getHeight()).thenReturn(100);
        DrawableImageViewTarget target = new DrawableImageViewTarget(mockView);
        GlideDrawable drawable = new MockAnimatedDrawable() {
            @Override
            public int getIntrinsicHeight() {
                return 100;
            }

            @Override
            public int getIntrinsicWidth() {
                return 150;
            }
        };
         target.onResourceReady(drawable, null);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Drawable drawable = (Drawable) invocation.getArguments()[0];
                assertFalse(drawable instanceof SquaringDrawable);
                return null;
            }
        }).when(mockView).setImageDrawable(any(Drawable.class));
        verify(mockView).setImageDrawable(any(BitmapDrawable.class));
    }

    @Test
    public void testDoesNotWrapInSquaringDrawableIfViewNotSquare() {
        ImageView mockView = mock(ImageView.class);
        when(mockView.getWidth()).thenReturn(100);
        when(mockView.getHeight()).thenReturn(150);
        DrawableImageViewTarget target = new DrawableImageViewTarget(mockView);
        GlideDrawable drawable = new MockAnimatedDrawable() {
            @Override
            public int getIntrinsicHeight() {
                return 100;
            }

            @Override
            public int getIntrinsicWidth() {
                return 100;
            }
        };
         target.onResourceReady(drawable, null);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Drawable drawable = (Drawable) invocation.getArguments()[0];
                assertFalse(drawable instanceof SquaringDrawable);
                return null;
            }
        }).when(mockView).setImageDrawable(any(Drawable.class));
        verify(mockView).setImageDrawable(any(BitmapDrawable.class));
    }

    @Test
    public void testStartsAnimatableDrawablesInOnReasourceReady() {
        MockAnimatedDrawable drawable = new MockAnimatedDrawable();
        DrawableImageViewTarget target = new DrawableImageViewTarget(new ImageView(Robolectric.application));
        target.onResourceReady(drawable, null);

        assertTrue(drawable.isStarted);
    }

    @Test
    public void testStartsAnimatableDrawablesOnStart() {
        MockAnimatedDrawable drawable = new MockAnimatedDrawable();
        DrawableImageViewTarget target = new DrawableImageViewTarget(new ImageView(Robolectric.application));
        target.onResourceReady(drawable, null);
        target.onStop();
        target.onStart();

        assertTrue(drawable.isStarted);
    }

    @Test
    public void testDoesNotStartNullDrawablesOnStart() {
        DrawableImageViewTarget target = new DrawableImageViewTarget(new ImageView(Robolectric.application));
        target.onStart();
    }

    @Test
    public void testStopsAnimatedDrawablesOnStop() {
        MockAnimatedDrawable drawable = new MockAnimatedDrawable();
        DrawableImageViewTarget target = new DrawableImageViewTarget(new ImageView(Robolectric.application));
        target.onResourceReady(drawable, null);
        target.onStop();

        assertFalse(drawable.isStarted);
    }

    @Test
    public void testDoesNotStopNullDrawablesOnStop() {
        DrawableImageViewTarget target = new DrawableImageViewTarget(new ImageView(Robolectric.application));
        target.onStop();
    }

    @Test
    public void testSetsLoopCountOnDrawable() {
        int maxLoopCount = 6;
        MockAnimatedDrawable drawable = new MockAnimatedDrawable();
        DrawableImageViewTarget target = new DrawableImageViewTarget(new ImageView(Robolectric.application),
                maxLoopCount);
        target.onResourceReady(drawable, null);
        assertEquals(maxLoopCount, drawable.loopCount);
    }

    private static class MockAnimatedDrawable extends GlideDrawable {
        private boolean isStarted;
        private int loopCount;

        @Override
        public void start() {
            isStarted = true;
        }

        @Override
        public void stop() {
            isStarted = false;
        }

        @Override
        public boolean isRunning() {
            return false;
        }

        @Override
        public void draw(Canvas canvas) {

        }

        @Override
        public void setAlpha(int i) {

        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return 0;
        }

        @Override
        public boolean isAnimated() {
            return false;
        }

        @Override
        public void setLoopCount(int loopCount) {
            this.loopCount = loopCount;
        }
    }
}