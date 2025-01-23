package com.yu.utils;

import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 文件工具类
 */
public final class FileUtil {
	/**
	 * Buffer的大小
	 */
	private static Integer BUFFER_SIZE = 1024 * 1024 * 10;

	public static MessageDigest MD5 = null;

	static {
		try {
			MD5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException ne) {
			ne.printStackTrace();
		}
	}

	/**
	 * 获取文件的md5
	 */
	public static String fileMD5(File file) {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			byte[] buffer = new byte[8192];
			int length;
			while ((length = fileInputStream.read(buffer)) != -1) {
				MD5.update(buffer, 0, length);
			}
			return new BigInteger(1, MD5.digest()).toString(16);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * 快速清空一个超大的文件
	 *
	 * @param file 需要处理的文件
	 * @return 是否成功
	 */
	public static boolean cleanFile(File file) {
		try (
				FileWriter fw = new FileWriter(file)
		) {
			fw.write("");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}


	/**
	 * 获取文件最后的修改时间
	 *
	 * @param file 需要处理的文件
	 * @return 返回文件的修改时间
	 */
	public static Date modifyTime(File file) {
		return new Date(file.lastModified());
	}


	/**
	 * 复制文件
	 *
	 * @param resourcePath 源文件
	 * @param targetPath   目标文件
	 * @return 是否成功
	 */
	public static boolean copy(String resourcePath, String targetPath) {
		File file = new File(resourcePath);
		return copy(file, targetPath);
	}


	/**
	 * 复制文件
	 * 通过该方式复制文件文件越大速度越是明显
	 *
	 * @param file       需要处理的文件
	 * @param targetFile 目标文件
	 * @return 是否成功
	 */
	public static boolean copy(File file, String targetFile) {
		try (
				FileInputStream fin = new FileInputStream(file);
				FileOutputStream fout = new FileOutputStream(new File(targetFile))
		) {
			FileChannel in = fin.getChannel();
			FileChannel out = fout.getChannel();

			//设定缓冲区
			ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

			while (in.read(buffer) != -1) {
				//准备写入，防止其他读取，锁住文件
				buffer.flip();
				out.write(buffer);

				//准备读取。将缓冲区清理完毕，移动文件内部指针
				buffer.clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}


	/**
	 * 删除一个文件
	 *
	 * @param file 需要处理的文件
	 * @return 是否成功
	 */
	public static boolean deleteFile(File file) {
		return file.delete();
	}


	/**
	 * 删除一个目录
	 *
	 * @param file 需要处理的文件
	 * @return 是否成功
	 */
	public static boolean deleteDir(File file) {
		List<File> files = listFileAll(file);
		if (valid(files)) {
			for (File f : files) {
				if (f.isDirectory()) {
					deleteDir(f);
				} else {
					deleteFile(f);
				}
			}
		}
		return file.delete();
	}


	/**
	 * 快速的删除超大的文件
	 *
	 * @param file 需要处理的文件
	 * @return 是否成功
	 */
	public static boolean deleteBigFile(File file) {
		return cleanFile(file) && file.delete();
	}


	/**
	 * 罗列指定路径下的全部文件
	 *
	 * @param path 需要处理的文件
	 * @return 包含所有文件的的list
	 */
	public static List<File> listFile(String path) {
		File file = new File(path);
		return listFile(file);
	}


	/**
	 * 罗列指定路径下的全部文件
	 *
	 * @param path  需要处理的文件
	 * @param child 是否罗列子文件
	 * @return 包含所有文件的的list
	 */
	public static List<File> listFile(String path, boolean child) {
		return listFile(new File(path), child);
	}


	/**
	 * 罗列指定路径下的全部文件
	 *
	 * @param path 需要处理的文件
	 * @return 返回文件列表
	 */
	public static List<File> listFile(File path) {
		List<File> list = new ArrayList<>();
		File[] files = path.listFiles();
		if (valid(files)) {
			for (File file : files) {
				if (file.isDirectory()) {
					list.addAll(listFile(file));
				} else {
					list.add(file);
				}
			}
		}
		return list;
	}


	/**
	 * 罗列指定路径下的全部文件
	 *
	 * @param path  指定的路径
	 * @param child 是否罗列子目录
	 */
	public static List<File> listFile(File path, boolean child) {
		List<File> list = new ArrayList<>();
		File[] files = path.listFiles();
		if (valid(files)) {
			for (File file : files) {
				if (child && file.isDirectory()) {
					list.addAll(listFile(file));
				} else {
					list.add(file);
				}
			}
		}
		return list;
	}


	/**
	 * 罗列指定路径下的全部文件包括文件夹
	 *
	 * @param path 需要处理的文件
	 * @return 返回文件列表
	 */
	public static List<File> listFileAll(File path) {
		List<File> list = new ArrayList<>();
		File[] files = path.listFiles();
		if (valid(files)) {
			for (File file : files) {
				list.add(file);
				if (file.isDirectory()) {
					list.addAll(listFileAll(file));
				}
			}
		}
		return list;
	}


	/**
	 * 罗列指定路径下的全部文件包括文件夹
	 *
	 * @param path   需要处理的文件
	 * @param filter 处理文件的filter
	 * @return 返回文件列表
	 */
	public static List<File> listFileFilter(File path, FilenameFilter filter) {
		List<File> list = new ArrayList<>();
		File[] files = path.listFiles();
		if (valid(files)) {
			for (File file : files) {
				if (file.isDirectory()) {
					list.addAll(listFileFilter(file, filter));
				} else {
					if (filter.accept(file.getParentFile(), file.getName())) {
						list.add(file);
					}
				}
			}
		}
		return list;
	}


	/**
	 * 获取文件后缀名
	 */
	public static String suffix(File file) {
		String fileName = file.getName();
		return fileName.substring(fileName.indexOf(".") + 1);
	}

	/**
	 * 判断对象是否有效
	 */
	private static boolean valid(Object[] objs) {
		return objs != null && objs.length != 0;
	}

	/**
	 * 判断一组集合是否有效
	 */
	public static boolean valid(Collection... cols) {
		for (Collection c : cols) {
			if (!valid(c)) {
				return false;
			}
		}
		return true;
	}
}