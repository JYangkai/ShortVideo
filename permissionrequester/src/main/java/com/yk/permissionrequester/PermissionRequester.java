package com.yk.permissionrequester;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;

public class PermissionRequester {
    private static final String TAG = "PermissionRequester";

    private final FragmentActivity fa;

    private PermissionFragment permissionFragment;

    private String[] permissions;

    private PermissionRequester(FragmentActivity fa) {
        this.fa = fa;
    }

    public static PermissionRequester build(FragmentActivity fa) {
        return new PermissionRequester(fa);
    }

    public PermissionRequester permission(String... permissions) {
        this.permissions = permissions;
        return this;
    }

    public void request(PermissionFragment.OnPermissionRequestListener onPermissionRequestListener) {
        initPermissionFragment(onPermissionRequestListener, new PermissionFragment.OnRequestListener() {
            @Override
            public void onStartRequest() {
                Log.d(TAG, "onStartRequest: ");
            }

            @Override
            public void onEndRequest() {
                Log.d(TAG, "onEndRequest: ");
                removePermissionFragment();
            }
        });
        addPermissionFragment();
    }

    private void initPermissionFragment(PermissionFragment.OnPermissionRequestListener onPermissionRequestListener,
                                        PermissionFragment.OnRequestListener onRequestListener) {
        if (permissionFragment == null) {
            permissionFragment = PermissionFragment.newInstance(permissions);
        }
        permissionFragment.setOnPermissionRequestListener(onPermissionRequestListener);
        permissionFragment.setOnRequestListener(onRequestListener);
    }

    private void addPermissionFragment() {
        if (fa == null) {
            return;
        }
        if (permissionFragment == null) {
            return;
        }
        fa.getSupportFragmentManager().beginTransaction()
                .add(permissionFragment, PermissionFragment.TAG)
                .commit();
    }

    private void removePermissionFragment() {
        if (fa == null) {
            return;
        }
        if (permissionFragment == null) {
            return;
        }
        fa.getSupportFragmentManager().beginTransaction()
                .remove(permissionFragment)
                .commit();
    }
}
