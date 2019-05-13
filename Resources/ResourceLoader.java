package Resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ResourceLoader {
	private static ResourceLoader rl = new ResourceLoader();

	private static String tempDirectory = null;
	
	public static String getResourcePath(String filename) {
		String path = new String();

		if (isRunningInJar() && tempDirectory != null) {
			path = tempDirectory + "/Resources/" + filename;
		}
		else {
			URL url = rl.getClass().getResource(filename);

			if (url != null) {
				try {
					path = URLDecoder.decode(url.getPath(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}

		return path;
	}

	public static String getExecutionPath() {
		String exePath = new String();

		try {
			exePath = URLDecoder.decode(
					rl.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(),
					"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return exePath;
	}

	public static boolean isRunningInJar() {
		File exeFile = new File(getExecutionPath());

		// We are in a JAR ?
		return exeFile.isFile();
	}

	public static void loadAll(String dataPath) throws Exception {
		if (!isRunningInJar())
			return;
		
		File exeFile = new File(getExecutionPath());

		if (tempDirectory == null) {
			tempDirectory = Files.createTempDirectory(exeFile.getName()).toFile().getPath();
		}
		
		File destDir = new File(tempDirectory);
		destDir.deleteOnExit();

		// Open the JAR
		JarFile jarFile = new JarFile(exeFile);
		Enumeration<JarEntry> entriesList = jarFile.entries();

		while (entriesList.hasMoreElements()) {
			JarEntry jarEntry = entriesList.nextElement();

			File entryFile = new File(destDir, jarEntry.getName());
			entryFile.deleteOnExit();

			// Filter interesting content
			if (!jarEntry.getName().startsWith("Resources/" + dataPath + "/"))
				continue;

			// Create directory tree
			if (!entryFile.getParentFile().exists()) {
				entryFile.getParentFile().mkdirs();
				entryFile.getParentFile().deleteOnExit();
			}
			
			if (jarEntry.isDirectory()) {
				continue;
			}

			// Write the files it the temporary directory
			InputStream is = jarFile.getInputStream(jarEntry);
			FileOutputStream fo = new FileOutputStream(entryFile);

			while (is.available() > 0) {
				fo.write(is.read());
			};
			
			fo.close();
			is.close();
		}

		jarFile.close();
	}
}