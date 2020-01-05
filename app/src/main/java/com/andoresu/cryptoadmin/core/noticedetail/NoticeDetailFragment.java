package com.andoresu.cryptoadmin.core.noticedetail;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.core.chargedetail.ChargeDetailPresenter;
import com.andoresu.cryptoadmin.core.notices.data.Notice;
import com.andoresu.cryptoadmin.core.notices.data.NoticeErrors;
import com.andoresu.cryptoadmin.utils.BaseFragment;
import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.andoresu.cryptoadmin.utils.MyUtils.HIDE_VIEW;
import static com.andoresu.cryptoadmin.utils.MyUtils.galleryIntent;
import static com.andoresu.cryptoadmin.utils.MyUtils.getPathFromURI;
import static com.andoresu.cryptoadmin.utils.MyUtils.glideRequestOptions;

public class NoticeDetailFragment extends BaseFragment implements NoticeDetailContract.View {

    private static final int IMAGE_PICKER_SELECT = 999;

    @BindView(R.id.noticeTitleTextInputLayout)
    TextInputLayout noticeTitleTextInputLayout;
    @BindView(R.id.noticeTitleEditText)
    EditText noticeTitleEditText;
    @BindView(R.id.noticeBodyTextInputLayout)
    TextInputLayout noticeBodyTextInputLayout;
    @BindView(R.id.noticeBodyEditText)
    EditText noticeBodyEditText;
    @BindView(R.id.noticeImageView)
    ImageView noticeImageView;
    @BindView(R.id.selectNoticeImageButton)
    Button selectNoticeImageButton;
    @BindView(R.id.saveNoticeButton)
    Button saveNoticeButton;

    private NoticeDetailContract.UserActionsListener actionsListener;

    private Notice notice = new Notice();

    private Uri selectedImageUri;

    public NoticeDetailFragment(){}

    public static NoticeDetailFragment newInstance(Notice notice) {

        Bundle args = new Bundle();

        NoticeDetailFragment fragment = new NoticeDetailFragment();
        fragment.setArguments(args);
        fragment.setTitle("Noticia");
        fragment.setNotice(notice);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){}
        actionsListener = new NoticeDetailPresenter(this, getContext());
    }

    @Override
    public void handleView() {
        selectNoticeImageButton.setOnClickListener(view1 -> selectImageFile());
        saveNoticeButton.setOnClickListener(view12 -> {
            updateNotice();
            if(notice.id == null){
                actionsListener.createNotice(notice, selectedImageUri);
            }else{
                actionsListener.updateNotice(notice, selectedImageUri);
            }
        });
        setData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_notice_detail;
    }

    private void setNotice(Notice notice) {
        if(notice != null){
            this.notice = notice;
        }
    }

    @Override
    public void showNotice(Notice notice) {
        setNotice(notice);
        setData();
    }

    private void setData() {


        noticeTitleEditText.setText(notice.title);
        noticeBodyEditText.setText(notice.body);

        if(notice.image != null){
            Glide.with(this)
                    .load(notice.image.url)
                    .apply(glideRequestOptions(getContext()))
                    .into(noticeImageView);
        }

    }

    private void updateNotice(){
        notice.title = noticeTitleEditText.getText().toString();
        notice.body = noticeBodyEditText.getText().toString();
    }

    @Override
    public void showNoticeErrors(NoticeErrors errors) {

    }

    private void selectImageFile(){
        startActivityForResult(galleryIntent(), IMAGE_PICKER_SELECT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER_SELECT  && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data.getData();
            final String path = getPathFromURI(selectedImageUri, getContext());
            if (path != null) {
                File file = new File(path);
                Glide.with(this)
                        .load(Uri.fromFile(file))
                        .into(noticeImageView);
            }
        }

    }
}
