## Screenshot

|                            Simple                            |                           Complex                            |
| :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![](https://wms-file-bucket.oss-cn-hangzhou.aliyuncs.com/1%E6%9C%8824%E6%97%A5%281%29.gif) | ![](https://wms-file-bucket.oss-cn-hangzhou.aliyuncs.com/1%E6%9C%8824%E6%97%A5.gif) |

## Getting started

1. Import library

   ```xml
   implementation 'io.github.xxmd:turntable:1.0.2'
   ```

2. Using in layout

   ```xml
   <io.github.xxmd.Turntable
           android:id="@+id/turn_table"
           android:layout_width="match_parent"
           android:layout_height="wrap_content"
           app:indicatorPointAngle="-45"
           app:labelTextSize="12sp" />
   ```

## Usage

```xml
<declare-styleable name="Turntable">
    <attr name="borderColor" format="color"/>
    <attr name="borderPercent" format="float"/>
    <attr name="labelDirection" format="enum">
        <enum name="Horizon" value="0"/>
        <enum name="CenterOut" value="1"/>
    </attr>
    <attr name="labelColor" format="color"/>
    <attr name="centerIndicator" format="reference"/>
    <attr name="indicatorPointAngle" format="float"/>
    <attr name="labelTextSize" format="dimension"/>
    <attr name="labelMarginPercent" format="float"/>
    <attr name="rotateDuration" format="integer"/>
</declare-styleable>
```

| Name                | Format    | Description                                                  |
| ------------------- | --------- | ------------------------------------------------------------ |
| borderColor         | color     | Outside border or stroke color                               |
| borderPercent       | float     | The percentage of the border of radius                       |
| labelDirection      | enum      | 1. Horizon: perpendicular to radius<br />2. CenterOut: along the radius from the center to the border |
| labelMarginPercent  | float     | The percentage of the distance from the text to the center of the circle to the radius |
| labelColor          | color     | Text label color                                             |
| labelTextSize       | color     | Label text size                                              |
| centerIndicator     | reference | Image of center indicator                                    |
| indicatorPointAngle | float     | The virtual indicator point angle                            |
| rotateDuration      | integer   | Duration of once rotate (Unit: ms)                           |