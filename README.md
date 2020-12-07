# ModalBottomSheetDialog
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16) 
[![](https://jitpack.io/v/invissvenska/ModalBottomSheetDialog.svg)](https://jitpack.io/#invissvenska/ModalBottomSheetDialog) 
[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-ModalBottomSheetDialog-green.svg?style=flat )]( https://android-arsenal.com/details/1/8133 ) 
<a href="https://github.com/invissvenska/ModalBottomSheetDialog/actions"><img alt="Build Status" src="https://github.com/invissvenska/ModalBottomSheetDialog/workflows/Android-Library%20CI/badge.svg"/></a> 
<span class="badge-buymeacoffee"><a href="https://www.paypal.com/paypalme/svenvandentweel/3" title="Donate to this project using Buy Me A Coffee"><img src="https://img.shields.io/badge/buy%20me%20a%20coffee-donate-yellow.svg" alt="Buy Me A Coffee donate button" /></a></span>  

## Prerequisites

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```gradle
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

## Dependency

Add this to your module's `build.gradle` file (make sure the version matches the JitPack badge above):

```gradle
dependencies {
    ...
    implementation 'com.github.invissvenska:ModalBottomSheetDialog:VERSION'
}
```

## Configuration

Implement the ModalBottomSheetDialog Listener interface on your Activity or Fragment:

```java
public class MainActivity extends AppCompatActivity implements ModalBottomSheetDialog.Listener {
    
    // some other code

    @Override
    public void onItemSelected(String tag, Item item) {
        Toast.makeText(getApplicationContext(), "Tag: " + tag + ", clicked on: " + item.getTitle(), 
            Toast.LENGTH_SHORT).show();
    }
}
```

```java
new ModalBottomSheetDialog.Builder()
    .setHeader(String title) // optional
    .setHeaderLayout(@LayoutRes int layoutResource) // optional (TextView must have id 'header' in layout)
    .add(@MenuRes int menuResource) // can be used more then once
    .setItemLayout(@LayoutRes int layoutResource) // optional (TextView with id 'title' or ImageView with id 'icon' must be defined in layout)
    .setColumns(int columns) // optional (default is 1)
    .setRoundedModal(boolean roundedModal) // optional (default is false)
    .show(FragmentManager fragmentManager, String tag);
```

Extend you theme with on of the DayNight variants to support a dark styled ModalBottomSheetDialog. For example `styles.xml`:
```xml
<resources>
    <!-- Base application theme. -->
    <style name="AppTheme" parent="Theme.MaterialComponents.DayNight.DarkActionBar">
        <!-- Customize your theme here. -->
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
    </style>

    <!-- other style declarations -->

</resources>
```

## Usage

### Fragment

When you use the ModalBottomSheetDialog in a Fragment and want to show the bottom dialog:
```java
dialog.show(getParentFragmentManager(), "WithHeader");
```
### Activity

When you use the ModalBottomSheetDialog in an Activity and want to show the bottom dialog:
```java
dialog.show(getSupportFragmentManager(), "WithHeader");
```

To create a ModalBottomSheetDialog and display it later in code:
``` java
ModalBottomSheetDialog dialog = new ModalBottomSheetDialog.Builder()
    .setHeader("Title of modal")
    .add(R.menu.options)
    .build();
// some other code in between
dialog.show(FragmentManager fragmentManager, "WithHeader");
```

To display a ModalBottomSheetDialog directly:
``` java
new ModalBottomSheetDialog.Builder()
    .setHeader("Title of modal")
    .add(R.menu.options)
    .show(FragmentManager fragmentManager, "WithHeader");
```

To display a ModalBottomSheetDialog with items from multiple menu resources:
``` java
new ModalBottomSheetDialog.Builder()
    .add(R.menu.options)
    .add(R.menu.options)
    .show(FragmentManager fragmentManager, "WithoutHeader");
```

To display a ModalBottomSheetDialog in a grid layout:
``` java
new ModalBottomSheetDialog.Builder()
    .setHeader("Grid bottom layout")
    .add(R.menu.lot_of_options)
    .setColumns(3)
    .show(FragmentManager fragmentManager, "Grid Layout");
```

To display a ModalBottomSheetDialog with custom layout:
``` java
new ModalBottomSheetDialog.Builder()
    .setHeader("Custom title and item layouts")
    .setHeaderLayout(R.layout.alternate_bottom_sheet_fragment_header)
    .add(R.menu.lot_of_options)
    .setItemLayout(R.layout.alternate_bottom_sheet_fragment_item)
    .setColumns(3)
    .show(FragmentManager fragmentManager, "Custom Layout");
```

To display a ModalBottomSheetDialog with rounded corners:
``` java
new ModalBottomSheetDialog.Builder()
    .setHeader("Rounded bottom layout")
    .add(R.menu.lot_of_options)
    .setRoundedModal(true)
    .show(FragmentManager fragmentManager, "Rounded Layout");
```

## Screenshots

**Please click the image below to enlarge.**

<img src="https://raw.githubusercontent.com/invissvenska/ModalBottomSheetDialog/master/media/collage.png">
