package siddur.common.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileSystemUtil {
	private static File home;
	
	public static File getHome(){
		if(home == null){
			home = new File(".");
		}
		return home;
	}
	
	public static void setHome(File _home){
		if(_home.isDirectory())
			home = _home;
	}
	
	public static File getToolDir(){
		return getChildDir("tools");
	}
	
	public static File getExtDir(){
		return getChildDir("ext");
	}
	
	public static File getFileServer(){
		return getChildDir("fileserver");
	}
	
	public static File getImageDir(){
		File file = new File(getFileServer(), "images");
		if(!file.isDirectory()){
			file.mkdir();
		}
		return file;
	}
	
	public static File getResourceDir(){
		return getChildDir("resource");
	}
	
	public static File getTempDir(){
		return getChildDir("temp");
	}
	
	public static String getRelativePath(String file, String parent){
		parent = parent.replace("\\", "/");
		file = file.replace("\\", "/");
		file = file.replace(parent, "");
		if(file.startsWith("/")){
			file = file.substring(1);
		}
		return file;
	}
	
	public static String getTempRelativePath(String file)throws IOException{
		return getRelativePath(file, getTempDir().getCanonicalPath());
	}
	
	public static String getRelativePath(String file) throws IOException{
		return getRelativePath(file, getHome().getCanonicalPath());
	}
	
	public static File getFileByRelativePath(String relativePath){
		return new File(getHome(), relativePath);
	}
	
	private static File getChildDir(String name){
		File f = new File(getHome(), name);
		if(!f.isDirectory()){
			f.mkdir();
		}
		return f;
	}
	
	public static File getOutputFileInTempDir(String path, String namespace) throws IOException{
		if(namespace == null){
			namespace = "";
		}
		return new File(FileSystemUtil.getTempDir().getCanonicalPath()
				+ "/" + namespace, path);
	}
	
	public static String copy2Temp(File dir)throws IOException{
		if(dir.isDirectory()){
			String dirname = dir.getName();
			FileUtils.copyDirectory(dir, new File(FileSystemUtil.getTempDir(), dirname));
			return dirname;
		}
		return null;
	}
	public static String copy2Temp(File f, String namespace)throws IOException{
		if(f.exists()){
			File downloadableDir = new File(FileSystemUtil.getTempDir(), namespace);
			if(!downloadableDir.isDirectory()){
				downloadableDir.mkdir();
			}
			File dest = new File(downloadableDir, f.getName());
			if(f.isFile())
				FileUtils.copyFile(f, dest);
			else
				FileUtils.copyDirectory(f, dest);
			return FileSystemUtil.getRelativePath(dest.getCanonicalPath(),
					FileSystemUtil.getTempDir().getCanonicalPath());
		}
		return null;
	}
	
	
	public static String getParentPath(String path){
		int slash = path.lastIndexOf("/");
		if(slash == -1){
			slash = path.lastIndexOf("\\");
		}
		if(slash != -1){
			path = path.substring(0, slash);
		}
		return path;
	}
	
	public static boolean isRelative(String s) throws IOException{
		return !((s.startsWith("/") 
				|| s.startsWith("\\") 
				|| s.startsWith(":", 1)));//absolute path
	}
	
	public static String getResourceContent(Object resId) throws IOException{
		File htm = new File(getResourceDir(), resId + ".htm");
		return FileUtils.readFileToString(htm);
	}
	
	public static void setResourceContent(String data, Object resId) throws IOException{
		File htm = new File(getResourceDir(), resId + ".htm");
		FileUtils.write(htm, data);
	}
}
