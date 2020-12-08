//package com.example.gestures;
//
//import android.content.Context;
//import android.hardware.camera2.CameraAccessException;
//import android.hardware.camera2.CameraManager;
//import android.os.Build;
//
//public class Utility {
//    Context context;
//    public boolean isSwitch = false;
//    public Utility(Context context){
//        this.context = context;
//    }
//    private void toggle (String cmd) throws CameraAccessException {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            CameraManager cameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
//            String cameraId = null;
//            if(cameraManager!=null){
//                cameraId = cameraManager.getCameraIdList()[0];
//                if(cmd.equals("on")){
//                    cameraManager.setTorchMode(cameraId, true);
//                    isSwitch = true;
//                }
//                else {
//                    cameraManager.setTorchMode(cameraId, false);
//                    isSwitch = false;
//                }
//            }
//        }
//    }
//}
