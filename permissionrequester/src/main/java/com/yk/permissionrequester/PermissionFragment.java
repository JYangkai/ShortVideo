package com.yk.permissionrequester;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermissionFragment extends Fragment {
    public static final String TAG = "PermissionFragment";

    private static final String EXTRA_PERMISSIONS = "extra_permissions";

    private static final int REQUEST_CODE = 0;

    private String[] permissions;

    public static PermissionFragment newInstance(String[] permissions) {
        PermissionFragment fragment = new PermissionFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray(EXTRA_PERMISSIONS, permissions);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
        initExtra();
        requestPermissions();
    }

    private void initExtra() {
        if (getArguments() != null) {
            permissions = getArguments().getStringArray(EXTRA_PERMISSIONS);
            Log.d(TAG, "initExtra: " + Arrays.toString(permissions));
        }
    }

    private void requestPermissions() {
        if (permissions == null) {
            onRequestSuccess(true);
            onEndRequest();
            return;
        }

        if (getContext() == null) {
            onRequestSuccess(false);
            onEndRequest();
            return;
        }

        List<String> permissionList = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (permissionList.isEmpty()) {
            onRequestSuccess(true);
            onEndRequest();
            return;
        }

        onStartRequest();

        requestPermissions(permissionList.toArray(new String[0]), REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: ");
        if (requestCode == 0) {
            List<String> grantedList = new ArrayList<>();
            List<String> deniedList = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    // 同意
                    grantedList.add(permission);
                }

                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    // 拒绝
                    deniedList.add(permission);
                }
            }
            boolean success = deniedList.isEmpty();

            onRequestSuccess(success);
            onGrantedList(grantedList);
            onDeniedList(deniedList);

            onEndRequest();
        }
    }

    private void onRequestSuccess(boolean success) {
        if (onPermissionRequestListener == null) {
            return;
        }
        onPermissionRequestListener.onRequestSuccess(success);
    }

    private void onGrantedList(List<String> grantedList) {
        if (onPermissionRequestListener == null) {
            return;
        }
        onPermissionRequestListener.onGrantedList(grantedList);
    }

    private void onDeniedList(List<String> deniedList) {
        if (onPermissionRequestListener == null) {
            return;
        }
        onPermissionRequestListener.onDeniedList(deniedList);
    }

    private void onStartRequest() {
        if (onRequestListener == null) {
            return;
        }
        onRequestListener.onStartRequest();
    }

    private void onEndRequest() {
        if (onRequestListener == null) {
            return;
        }
        onRequestListener.onEndRequest();
    }

    private OnPermissionRequestListener onPermissionRequestListener;

    private OnRequestListener onRequestListener;

    public void setOnPermissionRequestListener(OnPermissionRequestListener onPermissionRequestListener) {
        this.onPermissionRequestListener = onPermissionRequestListener;
    }

    public void setOnRequestListener(OnRequestListener onRequestListener) {
        this.onRequestListener = onRequestListener;
    }

    public interface OnPermissionRequestListener {
        void onRequestSuccess(boolean success);

        void onGrantedList(List<String> grantedList);

        void onDeniedList(List<String> deniedList);
    }

    public interface OnRequestListener {
        void onStartRequest();

        void onEndRequest();
    }
}
