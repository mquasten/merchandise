package de.mq.merchandise.opportunity.support;

import java.awt.image.BufferedImage;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.web.client.ResourceAccessException;

public class ResourceTemplateTest {

	private static final String TMP_FILE = "/tmp/test.txt";

	private static final byte[] CONTENT = "Hot kylie in wet dress as jpeg image".getBytes();

	private static final String ONE_PIXEL_IMAGE_URL = "file:src/test/resources/1X1-pixel.png";

	private ResourceOperations resourceOperations = new ResourceTemplateImpl();

	@Test
	public final void readImage() {

		final BufferedImage image = resourceOperations.readImage(ONE_PIXEL_IMAGE_URL);
		Assert.assertNotNull(image);
		Assert.assertEquals(1, image.getWidth());
		Assert.assertEquals(1, image.getHeight());
	}

	@Test(expected = ResourceAccessException.class)
	public final void readImageNotFound() throws IOException {

		resourceOperations.readImage(ONE_PIXEL_IMAGE_URL + ".dontLetMeGetMe");
	}

	@Test
	public final void copy() throws IOException {

		final InputStream is = Mockito.mock(InputStream.class);

		final boolean[] likeAVirgin = new boolean[] { true };

		Mockito.when(is.read(Mockito.any(byte[].class))).thenAnswer(new Answer<Integer>() {

			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {

				if (!likeAVirgin[0]) {
					return -1;
				}
				likeAVirgin[0] = false;
				byte[] result = (byte[]) invocation.getArguments()[0];
				for (int i = 0; i < CONTENT.length; i++) {
					result[i] = CONTENT[i];
				}

				return CONTENT.length;
			}
		});

		final OutputStream os = Mockito.mock(OutputStream.class);
		Assert.assertEquals(CONTENT.length, resourceOperations.copy(is, os));

		Mockito.verify(os).write(copy(CONTENT, ResourceTemplateImpl.BUFFER_SIZE), 0, CONTENT.length);

		// Assert.assertTrue(FileUtils.contentEquals(new
		// File(ONE_PIXEL_FILE_PATH), new File(
		// ResourceTemplateImpl.TARGET_FOLDER, UPLOADED_FILE)));

	}

	byte[] copy(final byte[] buffer, final int length) {
		byte[] result = new byte[length];
		for (int i = 0; i < buffer.length; i++) {
			result[i] = buffer[i];
		}

		return result;
	}

	@Test(expected = ResourceAccessException.class)
	public final void uploadIoException() throws IOException {
		final OutputStream os = Mockito.mock(OutputStream.class);
		final InputStream is = Mockito.mock(InputStream.class);

		Mockito.when(is.read(Mockito.any(byte[].class))).thenThrow(new IOException());

		resourceOperations.copy(is, os);
	}

	@Test
	public final void file() {
		Assert.assertTrue(resourceOperations.file(ONE_PIXEL_IMAGE_URL.replace("file:", "")).exists());
	}

	@Test
	public final void outputStream() throws IOException {
		try (final OutputStream os = resourceOperations.outputStream(TMP_FILE)) {
			os.write(CONTENT);
			Assert.assertNotNull(os);
		}

		Files.readAllBytes(Paths.get(TMP_FILE));
		Files.delete(Paths.get(TMP_FILE));

	}

}
