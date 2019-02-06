package com.tap.tapcolorskit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;

import android.graphics.Color;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tap.interfaces.FontColorInterface;

import java.util.ArrayList;

public class ColorPreferences extends ListPreference implements FontColorInterface {

    private final String                      fontPreview;
    private ArrayList<TapColor>               tapColorsArray;
    private TapColor                          selectedColor;

    public ColorPreferences(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.tap_ColorListPreferences);
        fontPreview = array.getString(R.styleable.tap_ColorListPreferences_tap_fontPreviewString);
        array.recycle();

        if(tapColorsArray == null){
            tapColorsArray = new ArrayList<>();
            prepareColorsArray();
        }

    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
          if(defaultValue!=null) {
              String bb = (defaultValue!=null && (((String) defaultValue).split("_").length >1 ) ?
                      ((String) defaultValue).split("_")[1]: "#000000");

              selectedColor = new TapColor( restoreValue ? this.getPersistedString(null) : (String) defaultValue, bb);

               this.updateSummary();
          }

    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }


    private void prepareColorsArray(){
        if(tapColorsArray == null) tapColorsArray = new ArrayList<>();
        TypedArray ta       = getContext().getResources().obtainTypedArray(R.array.colors);
        String[] colorNames = getContext().getResources().getStringArray(R.array.colorNames);

        for(int i=0; i < colorNames.length ;i++){
            String colorToUse     =  ta.getString(i);
            tapColorsArray.add(new TapColor(colorNames[i],colorToUse));
        }

    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        final CustomListPreferenceAdapter customListPreferenceAdapter = new CustomListPreferenceAdapter(tapColorsArray,
                fontPreview, selectedColor,this);

        builder.setAdapter(customListPreferenceAdapter, null);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(selectedColor!=null){
                    updateSummary();
                    persistString(selectedColor.getName()+"_"+selectedColor.getCode());
                }
            }
        });
    }

    void updateSummary() {
        if (selectedColor != null) {
            this.setSummary(selectedColor.getName() + "("+selectedColor.getCode()+")");
        }
    }

    @Override
    public void onClick(TapColor tapColor) {
        selectedColor = tapColor;
    }


    private static class CustomListPreferenceAdapter extends BaseAdapter {
        private final ArrayList<TapColor>        colors;
        private final String                     fontPreviewString;
        private final TapColor                   selectedColor;
        private final ArrayList<CheckBox>        colorCheckBoxes;
        private final ColorPreferences           listener;

        CustomListPreferenceAdapter(final ArrayList<TapColor> colors, final String fontPreviewString, final TapColor selectedColor
        , ColorPreferences listener) {
            this.colors            = colors;
            this.fontPreviewString = fontPreviewString;
            this.selectedColor     = selectedColor;
            this.colorCheckBoxes   = new ArrayList();
            this.listener = listener;
        }

        @Override
        public int getCount() {
            return colors.size();
        }

        @Override
        public Object getItem(final int position) {
            return colors.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
           return colors.size();
        }

        @Override
        public View getView(final int position,View convertView, final ViewGroup parent) {

             final Context context = parent.getContext();
             RelativeLayout          colorLayoutContainer;
             final TextView                checkedTextView;
             Button                  colorButton;
             final CheckBox          checkBox;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).
                        inflate(R.layout.custom_color_layout, parent, false);
            }

            checkedTextView      = convertView.findViewById(R.id.colorName);
            colorButton          = convertView.findViewById(R.id.colorButton);
            checkBox             = convertView.findViewById(R.id.checkBox);
            colorLayoutContainer = convertView.findViewById(R.id.colorlinearlayout);

            colorCheckBoxes.add(checkBox);
            // get current item to be displayed
            final TapColor color = (TapColor) getItem(position);

            checkedTextView.setText(color.getName());

            checkBox.setChecked(color.equals(selectedColor));

            colorButton.setBackgroundColor(Color.parseColor(color.getCode()));

            colorLayoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearOtherCheckBoxes();
                    checkBox.setChecked(true);
                    listener.onClick(color);
                }

            });


            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)clearOtherCheckBoxes();
                    checkBox.setChecked(isChecked);
                    listener.onClick(color);
                }
            });
            return convertView;
        }

        private void clearOtherCheckBoxes(){
            for(CheckBox cb :colorCheckBoxes)
                cb.setChecked(false);
        }
    }

}
