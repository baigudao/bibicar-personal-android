package com.wiserz.pbibi.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wiserz.pbibi.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by jackie on 2017/8/9 10:33.
 * QQ : 971060378
 * Used as : 一般工具类
 */
public class CommonUtil {

    public static ContentValues mContentValues = null;
    public final static String IMAGE_EXTENSION = ".jpg";
    public final static String GIF_EXTENSION = ".gif";

    /**
     * 获取diviceId,在测试升级时可能用到，上线时可以不再获取，可重写此方法返回一个固定的字符串，如：android，
     * 这样就可以不添加读取手机状态的权限
     * 需要增加 权限     <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        String deviceId = SPUtils.getInstance().getString(Constant.DEVICE_ID);
        if (TextUtils.isEmpty(deviceId)) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = telephonyManager.getDeviceId();
            if (deviceId == null) {
                deviceId = DeviceUtils.getAndroidID();// *** use for tablets
                if (deviceId == null) {
                    deviceId = "deviceId";
                }
                return deviceId;
            } else {
                SPUtils.getInstance().put(Constant.DEVICE_ID, deviceId);
                return deviceId;
            }
        } else {
            return deviceId;
        }
    }

    /**
     * 向SD卡写入数据
     *
     * @param str
     */
    public static void writeToSDCard(Context context, String str) {
        try {
            // 判断是否存在SD卡
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // 获取SD卡的目录
                File sdDire = Environment.getExternalStorageDirectory();
                FileOutputStream outFileStream = new FileOutputStream(sdDire.getCanonicalPath() + "/test.txt");
                outFileStream.write(str.getBytes());
                outFileStream.close();
                Toast.makeText(context, "储存成功！", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * obj -> json
     *
     * @param obj 对象
     * @return
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        return new Gson().toJson(obj);
    }

    /**
     * json -> obj
     *
     * @param beanClass 类
     * @param jsonStr   json字符串
     * @return
     */
    public static <T> T fromJson(Class<T> beanClass, String jsonStr) {
        T t = new Gson().fromJson(jsonStr, beanClass);
        return t;
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }

    /**
     * 判断list是否为空
     *
     * @param list
     * @return
     */
    public static boolean isListNullOrEmpty(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * 保存到相册
     *
     * @param bmp
     */
    public static String savePhotoToAppAlbum(Bitmap bmp, Context context) {
        String folder = createAppAlbumImagePath(context);
        FileOutputStream fout = null;
        BufferedOutputStream bos = null;
        try {
            fout = new FileOutputStream(folder);
            bos = new BufferedOutputStream(fout);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fout != null) {
                    fout.close();
                }
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mContentValues);
        mContentValues = null;
        return folder;
    }

    /**
     * 保存到相册
     */
    public static String saveGifToAppAlbum(byte[] bytes, Context context) {
        String folder = createAppGifAlbumImagePath(context);
        FileOutputStream fout = null;
        BufferedOutputStream bos = null;
        try {
            fout = new FileOutputStream(folder);
            bos = new BufferedOutputStream(fout);
            bos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fout != null) {
                    fout.close();
                }
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mContentValues);
        mContentValues = null;
        return folder;
    }

    public static String createAppAlbumImagePath(Context context) {
        String title = UUID.randomUUID().toString();
        String filename = title + GIF_EXTENSION;

        String dirPath = Environment.getExternalStorageDirectory() + "/" + context.getString(R.string.app_name);
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory())
            file.mkdirs();
        String filePath = dirPath + "/" + filename;
        ContentValues values = new ContentValues(7);
        values.put(MediaStore.Images.ImageColumns.TITLE, title);
        values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, filename);
        values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.ImageColumns.DATA, filePath);
        mContentValues = values;
        return filePath;
    }

    public static String createAppGifAlbumImagePath(Context context) {
        String title = UUID.randomUUID().toString();
        String filename = title + IMAGE_EXTENSION;

        String dirPath = Environment.getExternalStorageDirectory() + "/" + context.getString(R.string.app_name);
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) file.mkdirs();
        String filePath = dirPath + "/" + filename;
        ContentValues values = new ContentValues(7);
        values.put(MediaStore.Images.ImageColumns.TITLE, title);
        values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, filename);
        values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/gif");
        values.put(MediaStore.Images.ImageColumns.DATA, filePath);
        mContentValues = values;
        return filePath;
    }

    /**
     * @param context
     * @param defaultId
     * @param path
     * @param iv
     */
    public static void loadImage(Context context, int defaultId, String path, ImageView iv) {
        if (iv.getMeasuredWidth() > 0 && iv.getMeasuredHeight() > 0) {
            Glide.with(context)
                    .load(path)
                    .override(iv.getMeasuredWidth(), iv.getMeasuredHeight())
                    .error(defaultId)//load失敗的Drawable
                    .placeholder(defaultId)
                    .centerCrop()//中心切圖, 會填滿
                    .fitCenter()//中心fit, 以原本圖片的長寬為主
                    .crossFade()
                    .into(iv);
        } else {
            Glide.with(context)
                    .load(path)
                    .error(defaultId)//load失敗的Drawable
                    .placeholder(defaultId)
                    .centerCrop()//中心切圖, 會填滿
                    .fitCenter()//中心fit, 以原本圖片的長寬為主
                    .crossFade()
                    .into(iv);
        }

    }

    /**
     * @param context
     * @param defaultId
     * @param uri
     * @param iv
     */
    public static void loadImage(Context context, int defaultId, Uri uri, ImageView iv) {
        if (iv.getMeasuredWidth() > 0 && iv.getMeasuredHeight() > 0) {
            Glide.with(context)
                    .load(uri)
                    .placeholder(defaultId)
                    .override(iv.getMeasuredWidth(), iv.getMeasuredHeight())
                    .error(defaultId)//load失敗的Drawable
                    .centerCrop()//中心切圖, 會填滿
                    .fitCenter()//中心fit, 以原本圖片的長寬為主
                    .crossFade()
                    .into(iv);
        } else {
            Glide.with(context)
                    .load(uri)
                    .placeholder(defaultId)
                    .error(defaultId)//load失敗的Drawable
                    .centerCrop()//中心切圖, 會填滿
                    .fitCenter()//中心fit, 以原本圖片的長寬為主
                    .crossFade()
                    .into(iv);
        }
    }

    /**
     * 根据给定的宽高取图片
     *
     * @param imageFile
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getBitmapFromFile(File imageFile, int width, int height) {
        if (width == 0 || height == 0) {
            return null;
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        int[] bound = getBitmapBound(imageFile.getAbsolutePath());
        if (width == 0 || height == 0) {
            return null;
        }
        int degree = getBitmapDegree(imageFile.getAbsolutePath());
        int wmRatio, hmRatio;
        if (degree % 180 == 0) {
            wmRatio = bound[0] / width;
            hmRatio = bound[1] / height;
        } else {
            wmRatio = bound[1] / width;
            hmRatio = bound[0] / height;
        }
        if (wmRatio > 1 || hmRatio > 1) {
            if (wmRatio > hmRatio) {
                opts.inSampleSize = wmRatio;
            } else {
                opts.inSampleSize = hmRatio;
            }
        }
        Bitmap bmp = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), opts);
        if (degree % 360 != 0 && bmp != null) {
            Matrix matrix = new Matrix();
            matrix.setRotate(degree, bmp.getWidth() / 2, bmp.getHeight() / 2);
            Bitmap newBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            bmp.recycle();
            return newBmp;
        }
        return bmp;
    }

    public static int[] getBitmapBound(String imageFile) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile, opts);
        int[] bound = new int[2];
        bound[0] = opts.outWidth;
        bound[1] = opts.outHeight;
        return bound;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static String createImagePath(Context context) {
        String fileName = UUID.randomUUID().toString() + ".jpg";
        String dirPath = Environment.getExternalStorageDirectory() + "/Android/data/" + context.getPackageName() + "/download";
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory())
            file.mkdirs();
        String filePath = dirPath + "/" + fileName;
        return filePath;
    }

    public static String getSaveFilePath(Context context, String fileName) {
        String dirPath = Environment.getExternalStorageDirectory() + "/Android/data/" + context.getPackageName() + "/download";
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory())
            file.mkdirs();
        String filePath = dirPath + "/" + fileName;
        return filePath;
    }

    /**
     * 保存JPG图片
     *
     * @param bmp
     */
    public static String saveJpegByFileName(Bitmap bmp, String fileName, Context context) {
        String folder = getSaveFilePath(context, fileName);
        FileOutputStream fout = null;
        BufferedOutputStream bos = null;
        try {
            fout = new FileOutputStream(folder);
            bos = new BufferedOutputStream(fout);
            bmp.compress(Bitmap.CompressFormat.JPEG, 70, bos);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fout != null) {
                    fout.close();
                }
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return folder;
    }

    /**
     * 保存JPG图片
     *
     * @param bmp
     */
    public static String saveJpeg(Bitmap bmp, Context context) {
        String folder = createImagePath(context);
        FileOutputStream fout = null;
        BufferedOutputStream bos = null;
        try {
            fout = new FileOutputStream(folder);
            bos = new BufferedOutputStream(fout);
            bmp.compress(Bitmap.CompressFormat.JPEG, 70, bos);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fout != null) {
                    fout.close();
                }
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return folder;
    }


    /**
     * dp转px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}