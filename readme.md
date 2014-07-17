
# 概要

> 该项目包含了许许多多Android小工程。编写它们的目的是对Android中各个特性进行更细致的学习！
> 关于这些工程的详细介绍，请查看我的博客：[wangkuiwu的博客](http://wangkuiwu.github.com)

> **目录**  
> **1**. [Android培训](#anchor1)  
> **1.1**. [环境搭建和建立Android工程](#anchor1_1)  
> **1.1**. [添加ActionBar](#anchor1_2)  
> **1.1**. [支持多设备](#anchor1_3)  
> **1.1**. [管理Activity的生命周期](#anchor1_4)  
> **1.5**. [使用Fragment打造灵活的UI](#anchor1_5)  
> **1.6**. [保存数据](#anchor1_6)  
> **1.7**. [数据交互](#anchor1_7)  
> **2**. [共享数据](#anchor2)  
> **2.1**. [共享简单数据](#anchor2_1)  
> **2.2**. [通过FileProvider共享文件](#anchor2_2)  
> **3**. [Widget控件](#anchor3)  
> **3.1**. [Button](#anchor3_1)  
> **3.2**. [RadioButton](#anchor3_2)  
> **3.3**. [ToggleButton](#anchor3_3)  
> **3.4**. [CheckBox](#anchor3_4)  
> **3.5**. [EditText](#anchor3_5)  
> **3.6**. [Spinner](#anchor3_6)  
> **3.7**. [DatePicker和TimePicker](#anchor3_7)  
> **3.8**. [ListView](#anchor3_8)  
> **3.9**. [TabHost](#anchor3_9)  
> **3.10**. [Toast](#anchor3_10)  
> **4**. [UI组件](#anchor4)  
> **4.1**. [Menu菜单](#anchor4_1)  
> **4.2**. [ActionBar](#anchor4_2)  
> **4.3**. [Dialog对话框](#anchor4_3)  
> **4.4**. [Style和Theme](#anchor4_4)  
> **4.5**. [Search查询功能](#anchor4_5)  
> **4.6**. [自定义View视图](#anchor4_6)  
> **4.7**. [ViewPager](#anchor4_7)  
> **4.8**. [Notification](#anchor4_8)  
> **5**. [Android四大组成部分](#anchor5)  
> **5.1**. [Activity](#anchor5_1)  
> **5.1.1**. [Activity运行模式](#anchor5_1_1)  
> **5.1.2**. [Fragment碎片](#anchor5_1_2)  
> **5.2**. [Intent](#anchor5_2)  
> **5.2.1**. [常用Intent](#anchor5_2_1)  
> **5.3**. [ContentProvider](#anchor5_3)  


<a name="anchor1"></a>
# Android培训

<a name="anchor1_1"></a>
## 1.环境搭建和建立Android工程

> 链接：[博客中关于创建第一个程序的步骤说明](http://wangkuiwu.github.io/2014/05/26/BuildYourFirstApp/)

示例一：[最简单的Android工程][link_getstarted_01]

> 说明：该示例是最简单的Android工程，但是它包行了Android的基本组成部分。



示例二：[具有简单UI的工程][link_getstarted_02]

> 说明：该示例的Activity包含了：按钮(Button) 和 编辑器(EditText)。


示例三：[Activity跳转][link_getstarted_03]

> 说明：该示例演示了如何从一个Activity跳转到另一个Activity。




<a name="anchor1_2"></a>
## 2.添加ActionBar

示例一：[添加ActionBar][link_actionbar_01]

> 说明：该示例演示如何添加ActionBar。

> 链接：[博客中关于该示例的详细介绍](http://wangkuiwu.github.io/2014/05/26/ActionBar/)。


示例二：[自定义ActionBar][link_actionbar_02]

> 说明：该示例演示如何自定义ActionBar。

> 链接：[博客中关于该示例的详细介绍](http://wangkuiwu.github.io/2014/05/26/ActionBar/)。



示例三：[覆盖ActionBar][link_actionbar_03] 

> 说明：该示例Activity和ActionBar相互重叠时，如何处理

> 链接：[博客中关于该示例的详细介绍](http://wangkuiwu.github.io/2014/05/26/ActionBar/)。





<a name="anchor1_3"></a>
## 3.支持多设备

示例一：[多语言支持][link_different_01]

> 说明：该APK同时支持"英文/简体中文/繁体中文"三种语言。

> 链接：[博客中关于该示例的详细介绍](http://wangkuiwu.github.io/2014/05/27/SupportDifferentDevice/)。



<a name="anchor1_4"></a>
## 4.管理Activity的生命周期

示例一：[基本生命周期][link_lifecycle_01]

> 说明：该示例演示了Activity的基本生命周期。

> 链接：[博客中关于该示例的详细介绍](http://wangkuiwu.github.io/2014/05/29/ActivityLifecycle/)。


示例二：[快速灭亡][link_lifecycle_02]

> 说明：该示例演示了Activity的快速灭亡的流程。

> 链接：[博客中关于该示例的详细介绍](http://wangkuiwu.github.io/2014/05/29/ActivityLifecycle/)。


示例三：[基本生命周期][link_lifecycle_03]

> 说明：该示例演示了onSaveInstanceState和onRestoreInstanceState的基本用法。

> 链接：[博客中关于该示例的详细介绍](http://wangkuiwu.github.io/2014/05/29/ActivityLifecycle/)。






<a name="anchor1_5"></a>
## 5. 使用Fragment打造灵活的UI

Fragment有两种被Activity导入的方式：**静态加载** 和 **动态加载**。


示例一：[静态加载Fragment][link_fragment_01]

> 说明：该示例演示了Fragment的静态加载方式。自定义一个Fragment的子类ExampleFragment，然后通过layout添加ExampleFragment。

> 链接：[博客中关于该示例的详细介绍](http://wangkuiwu.github.io/2014/05/30/FragmentBasic/)。


示例二：[动态加载Fragment][link_fragment_02]

> 说明：该示例演示了Fragment的动态加载方式。自定义一个Fragment的子类ExampleFragment，然后通过Activity中通过FragmentManager来管理即可。

> 链接：[博客中关于该示例的详细介绍](http://wangkuiwu.github.io/2014/05/30/FragmentBasic/)。



示例三：[Fragment生命周期][link_fragment_03]

> 说明：该示例演示了Fragment的生命周期。

> 链接：[博客中关于该示例的详细介绍](http://wangkuiwu.github.io/2014/05/30/FragmentBasic/)。


示例四：[Google给出的Fragment示例][link_fragment_04]

> 说明：该示例是Google官网上的示例，它与"[Fragment简介部分][link_fragment_google02]"相对应。Google官网关于Fragment的讲解内容包括三部分：创建Fragment，用Fragment创建灵活的UI，Fragments之间的通信。

> 链接：[博客中关于该示例的详细介绍](http://wangkuiwu.github.io/2014/05/30/FragmentBasic/)。



<a name="anchor1_6"></a>
## 6. 保存数据

Android主要有4种保存数据的方式：SharedPreferences，File文件，数据库和ContentProvider。这里主要介绍前面三种。[更多内容](http://wangkuiwu.github.io/2014/05/30/SavingData/)



### 6.1 SharedPreferences

示例一：[SharedPreferences基本用法][link_sharedpreference_01]

> 说明：该示例演示了SharedPreferences的基本用法。本例使用的SharedPreferences是APK默认的，而非自己指定名称。

示例二：[APK默认的SharedPreferences][link_sharedpreference_02]

> 说明：该示例演示了多个Activity调用APK默认的SharedPreferences。

示例三：[指令名称的SharedPreferences][link_sharedpreference_03]

> 说明：该示例演示了指定名称的SharedPreferences。

### 6.2 保存File文件

示例一：[保存File基本用法][link_savefile_01]

> 说明：该示例演示了保存File文件的基本用法。包括"使用Java的File接口"和"使用Android的File接口"两种方式操作文件。

### 6.3 保存Database数据库

示例一：[保存Database数据库的基本用法][link_savedatabase_01]

> 说明：该示例演示了保存Database数据库的基本用法。


<a name="anchor1_7"></a>
## 7. 数据交互

本节介绍Activity之间交互的几个相关内容。包括：启动另一个Acivity，启动另一个Activity并获取结果，接受其他Intent等内容。[更多内容](http://wangkuiwu.github.io/2014/05/31/Intent/)


### 7.1 启动另一个Acivity

示例一：[显式跳转][link_interact_intent_01]

> 说明：该示例演示了显式Intent的基本用法。

示例二：[隐式跳转][link_interact_intent_02]

> 说明：该示例演示了隐式Intent的基本用法。

示例三：[Intent数据][link_interact_intent_03]

> 说明：该示例演示了常用程序的Intent的使用用法。包括：打开网页、发送邮件，查看闹钟等。

示例四：[认证Intent][link_interact_intent_04]

> 说明：该示例演示了"Intent是否被接收，被多少个Activity接收"。

示例五：[跳转][link_interact_intent_05]

> 说明：该示例演示了"只有当Intent被多个Acvitity接收时，弹出选择框"。


### 7.2 启动另一个Acivity并获取结果

示例一：[启动Activity并获取结果][link_interact_startactivity_01]

> 说明：该示例演示了通过startActivityForResult()启动另一个Acitivity并获取结果的方法。

### 7.3 接收并解析Intent

示例一：[接收并解析Intent][link_interact_receiveintent_01]

> 说明：该示例演示了通过接收并解析Intent的基本用法。



<a name="anchor2"></a>
# 共享数据

Android中共享数据的手段主要包括：通过Intent发送简单数据，通过FileProvider共享文件，通过ContentProvider共享结构较复杂的内容。

<a name="anchor2_1"></a>
## 1. 共享简单数据

### 1.1 共享文本

点击查看：[更多内容](http://wangkuiwu.github.io/2014/06/03/SharingSimpleData/)

示例一：[共享文本][link_share_text_01]

> 说明：该示例演示了共享文本的基本用法。

### 1.2 Menu共享菜单

示例二：[Menu共享菜单][link_share_text_02]

> 说明：该示例演示了在ActionBar的menu菜单中添加系统自带的共享菜单的方法。

<a name="anchor2_2"></a>
## 2. 通过FileProvider共享文件

点击查看：[更多内容](http://wangkuiwu.github.io/2014/06/04/SharingSimpleData/)

示例一：[共享文件][link_share_file_01]

> 说明：该示例演示了通过FileProvider共享图片文件的基本用法。



<a name="anchor3"></a>
# Widget控件

<a name="anchor3_1"></a>
## 1. [Button][link_uiwidget_button_01]

> [详细介绍](http://wangkuiwu.github.io/2014/06/06/Button/)

<a name="anchor3_2"></a>
## 2. [RadioButton][link_uiwidget_radiobutton_01]

> [详细介绍](http://wangkuiwu.github.io/2014/06/09/RadioButton/)

<a name="anchor3_3"></a>
## 3. [ToggleButton][link_uiwidget_togglebutton_01]

> [详细介绍](http://wangkuiwu.github.io/2014/06/10/ToggleButton/)

<a name="anchor3_4"></a>
## 4. [CheckBox][link_uiwidget_checkbox_01]

> [详细介绍](http://wangkuiwu.github.io/2014/06/08/CheckBox/)

<a name="anchor3_5"></a>
## 5. [EditText][link_uiwidget_edittext_01]

> [详细介绍](http://wangkuiwu.github.io/2014/06/07/EditText/)

<a name="anchor3_6"></a>
## 6. [Spinner][link_uiwidget_spinner_01]

> [详细介绍](http://wangkuiwu.github.io/2014/06/11/Spinner/)

<a name="anchor3_7"></a>
## 7. [DatePicker和TimePicker][link_uiwidget_picker_01]

> [详细介绍](http://wangkuiwu.github.io/2014/06/12/Pickers/)

<a name="anchor3_8"></a>
## 8. ListView

> [详细介绍](TODO)

示例一：[ListView的LinearLayout布局][link_uiwidget_listview_01]

示例二：[ListView的RelativeLayout布局][link_uiwidget_listview_02]


<a name="anchor3_9"></a>
## 9. TabHost

> [详细介绍](http://wangkuiwu.github.io/2014/06/13/TabHost/)

示例一：[TabHost通过Fragment设置Tab][link_uiwidget_tabhost_01]

示例二：[TabHost通过layout设置Tab][link_uiwidget_tabhost_02]


<a name="anchor3_10"></a>
## 10. Toast

> [详细介绍](http://wangkuiwu.github.io/2014/06/18/Toast/)

示例一：[Toast基本用法][link_uiwidget_toast_01]

示例二：[Toast自定义layout][link_uiwidget_toast_02]





<a name="anchor4"></a>
# UI组件

<a name="anchor4_1"></a>
## 1. Menu菜单

> [详细介绍](http://wangkuiwu.github.io/2014/06/13/Menu/)

示例一：[基本菜单的用法][link_ui_menu_01]

示例二：[悬浮菜单的用法][link_ui_menu_02]


<a name="anchor4_2"></a>
## 2. ActionBar菜单

> [详细介绍](http://wangkuiwu.github.io/2014/06/14/ActionBar/)

示例一：[ActionBar的基本用法][link_ui_actionbar_01]

示例二：[ActionBar的隐藏API][link_ui_actionbar_02]

示例三：[ActionBar返回上一个Activity][link_ui_actionbar_03]

示例四：[ActionBar和Tab绑定使用][link_ui_actionbar_04]


<a name="anchor4_3"></a>
## 3. Dialog对话框

> [详细介绍](http://wangkuiwu.github.io/2014/06/15/Dialog/)

示例一：[DialogFragment的基本用法][link_ui_dialog_01]

示例二：[自定义Dialog布局][link_ui_dialog_02]

示例三：[Dialog和Activity通信][link_ui_dialog_03]


<a name="anchor4_4"></a>
## 4. Style和Theme

> [详细介绍](http://wangkuiwu.github.io/2014/06/16/StylesAndThemes/)

示例一：[Style样式][link_ui_style_01]

示例二：[Theme主题][link_ui_theme_01]


<a name="anchor4_5"></a>
## 5. Search查询功能

Android提供了两种内置的Activity查询组件：Search Dialog和Search Widget。

> [详细介绍](http://wangkuiwu.github.io/2014/06/17/Search/)

示例一：[Search Dialog][link_ui_search_01]

示例二：[Search Widget][link_ui_search_02]

示例三：[最近查询记录][link_ui_search_03]


<a name="anchor4_6"></a>
## 6. 自定义View视图

示例一：[基本的自定义View][link_ui_selfview_01]

> [详细介绍](http://wangkuiwu.github.io/2014/06/20/View/)

示例二：[onMeasure的基本用法][link_ui_selfview_02]

> [详细介绍](http://wangkuiwu.github.io/2014/06/20/View-OnMeasure/)

示例三：[onLayout的基本用法][link_ui_selfview_03]

> [详细介绍](http://wangkuiwu.github.io/2014/06/20/View-OnLayout/)

示例四：[位置说明][link_ui_selfview_04]

> [详细介绍](http://wangkuiwu.github.io/2014/06/20/View-layout/)


<a name="anchor4_7"></a>
## 7. ViewPager

> [详细介绍](http://wangkuiwu.github.io/2014/06/11/ViewPager/)

示例一：[ViewPager基本用法][link_ui_viewpager_01]

示例二：[ViewPager使用Fragment][link_ui_viewpager_02]


<a name="anchor4_8"></a>
## 8. [Notification][TODO]




<a name="anchor5"></a>
# Android四大组成部分

<a name="anchor5_1"></a>
## 1. Activity

<a name="anchor5_1_1"></a>
### 1.1 Activity运行模式

#### 1.1.1 Lauch Mode

> [详细介绍](http://wangkuiwu.github.io/2014/06/26/LaunchMode/)

示例一：[standard模式][link_activity_mode_01]

示例二：[SingleTop模式][link_activity_mode_02]

示例三：[SingleTask模式][link_activity_mode_03]

示例四：[SingleInstance模式][link_activity_mode_04]

#### 1.2 与Launch Mode相关的Intent Flag

> [详细介绍](http://wangkuiwu.github.io/2014/06/26/IntentFlag/)

示例一：[FLAG_ACTIVITY_SINGLE_TOP标签][link_activity_flag_mode_01]

示例二：[FLAG_ACTIVITY_NEW_TASK标签][link_activity_flag_mode_02]

示例三：[FLAG_ACTIVITY_CLEAR_TOP标签][link_activity_flag_mode_03]

示例四：[FLAG_ACTIVITY_CLEAR_TASK标签][link_activity_flag_mode_04]



<a name="anchor5_1_2"></a>
### 1.2 Fragment碎片

#### 1.2.1 ListFragment

> [详细介绍](http://wangkuiwu.github.io/2014/06/23/ListFragment/)

示例一：[ListFragment的基本使用][link_activity_fragment_01]

示例二：[ListFragment自定义布局][link_activity_fragment_02]



#### 1.2.1 PreferenceFragment


示例一：[PreferenceFragment的基本使用][link_activity_preffragment_01]

> [详细介绍](http://wangkuiwu.github.io/2014/06/23/PreferenceFragment/)

示例二：[PreferenceFragment的自定义属性][link_activity_preffragment_02]

> [详细介绍](http://wangkuiwu.github.io/2014/06/23/SelfDefine-ListPreference/)

示例二：[PreferenceFragment的自定义属性和自定义API][link_activity_preffragment_03]



<a name="anchor5_2"></a>
## 2. Intent

<a name="anchor5_2_1"></a>
### 2.1 常用Intent

示例一：[闹钟和秒表][link_intent_common_01]

示例二：[行程][link_intent_common_02]

示例三：[照相][link_intent_common_03]

示例四：[联系人][link_intent_common_04]

示例五：[E-Mail][link_intent_common_05]

示例六：[图库][link_intent_common_06]

示例七：[网页][link_intent_common_07]


<a name="anchor5_3"></a>
## 3. ContentProvider

示例一：[ContentProvider基本示例][link_contentprovidr_01]

> [详细介绍](http://wangkuiwu.github.io/2014/07/06/ContentProvider-Usage/)

示例二：[ContentProvider与Persmission绑定使用][link_contentprovidr_02]

> [详细介绍](http://wangkuiwu.github.io/2014/07/06/ContentProvider-Permission/)









[link_getstarted_01]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/01_building_your_first_app/01_create_an_android_project
[link_getstarted_02]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/01_building_your_first_app/02_building_a_simple_user_interface
[link_getstarted_03]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/01_building_your_first_app/03_starting_another_activity
[link_actionbar_01]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/02_action_bar/02_adding_actionbar
[link_actionbar_02]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/02_action_bar/03_customzation_bar
[link_actionbar_03]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/02_action_bar/04_overlay_bar
[link_different_01]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/03_different_devices/01_languages
[link_lifecycle_01]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/04_activity_lifecycle/LifeCycle_01
[link_lifecycle_02]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/04_activity_lifecycle/LifeCycle_02
[link_lifecycle_03]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/04_activity_lifecycle/LifeCycle_03
[link_fragment_01]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/05_fragments/implemention_01
[link_fragment_02]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/05_fragments/implemention_02
[link_fragment_03]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/05_fragments/lifecycle
[link_fragment_04]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/05_fragments/google
[link_sharedpreference_01]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/06_saving_data/01_shared_preferences/01_basic
[link_sharedpreference_02]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/06_saving_data/01_shared_preferences/02_activity_default
[link_sharedpreference_03]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/06_saving_data/01_shared_preferences/SaveData
[link_savefile_01]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/06_saving_data/02_files/FileTest
[link_savedatabase_01]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/06_saving_data/03_database/FeedReader
[link_interact_intent_01]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/07_interacting_with_other_activity/01_send_user_to_another_apk/01_explicit_jump/JumpTest
[link_interact_intent_02]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/07_interacting_with_other_activity/01_send_user_to_another_apk/02_implicit_jump/JumpTest
[link_interact_intent_03]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/07_interacting_with_other_activity/01_send_user_to_another_apk/03_intent_data/JumpTest
[link_interact_intent_04]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/07_interacting_with_other_activity/01_send_user_to_another_apk/04_verify_intent/JumpTest
[link_interact_intent_05]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/07_interacting_with_other_activity/01_send_user_to_another_apk/05_activies_choose/JumpTest
[link_interact_startactivity_01]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/07_interacting_with_other_activity/02_start_activity_for_result/StartActivity
[link_interact_receiveintent_01]: https://github.com/wangkuiwu/android_applets/tree/master/training/01_getting_started/07_interacting_with_other_activity/03_accept_other_intent/JumpTest
[link_share_text_01]: https://github.com/wangkuiwu/android_applets/tree/master/training/02_sharing_data/01_share_simple_data/01_simple_data_to_other_app/SendText
[link_share_text_02]: https://github.com/wangkuiwu/android_applets/tree/master/training/02_sharing_data/01_share_simple_data/03_add_easy_share_action/SendText
[link_share_file_01]: https://github.com/wangkuiwu/android_applets/tree/master/training/02_sharing_data/02_share_file/ShareFile
[link_uiwidget_button_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/widgets/Button/ButtonTest
[link_uiwidget_radiobutton_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/widgets/RadioButton/RadioTest
[link_uiwidget_togglebutton_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/widgets/ToggleButton/ToggleTest
[link_uiwidget_checkbox_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/widgets/CheckBox/CheckBoxTest
[link_uiwidget_edittext_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/widgets/EditText/EditorTest
[link_uiwidget_spinner_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/widgets/Spinner/SpinnerTest
[link_uiwidget_picker_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/widgets/Pickers/PickerTest
[link_uiwidget_listview_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/widgets/ListView/01_linear/ShareFile
[link_uiwidget_listview_02]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/widgets/ListView/02_relative/ShareFile
[link_uiwidget_tabhost_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/widgets/TabHost/01_basic/TabTest
[link_uiwidget_tabhost_02]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/widgets/TabHost/02_TabContentView/TabTest
[link_uiwidget_toast_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/widgets/Toast/01_basic/ToastTest
[link_uiwidget_toast_02]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/widgets/Toast/02_selflayout/ToastTest
[link_ui_menu_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/menu/01_basic/MenuTest
[link_ui_menu_02]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/menu/02_float_menu/MenuTest
[link_ui_actionbar_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/actionbar/01_basic/BarTest
[link_ui_actionbar_02]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/actionbar/02_hide/BarTest
[link_ui_actionbar_03]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/actionbar/03_homeup/BarTest
[link_ui_actionbar_04]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/actionbar/04_tab/BarTest
[link_ui_dialog_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/dialog/01_dialogfragment/DialogTest
[link_ui_dialog_02]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/dialog/02_self_layout/DialogTest
[link_ui_dialog_03]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/dialog/03_callback/DialogTest
[link_ui_style_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/style_theme/01_style/StyleTest
[link_ui_theme_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/style_theme/02_theme/ThemeTest
[link_ui_search_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/search/01_search_dialog/SearchTest
[link_ui_search_02]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/search/02_search_widget/SearchTest
[link_ui_search_03]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/search/03_search_suggestion/SearchTest
[link_ui_selfview_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/self_view/01_basic
[link_ui_selfview_02]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/self_view/03_onmeasure
[link_ui_selfview_03]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/self_view/05_viewgroup
[link_ui_selfview_04]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/self_view/04_position
[link_ui_viewpager_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/viewpager/01_basic
[link_ui_viewpager_02]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/ui/viewpager/02_fragment_viewpager
[link_activity_mode_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/app_components/activity/launch_mode/01_standard
[link_activity_mode_02]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/app_components/activity/launch_mode/02_single_top
[link_activity_mode_03]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/app_components/activity/launch_mode/03_single_task
[link_activity_mode_04]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/app_components/activity/launch_mode/04_single_instance
[link_activity_flag_mode_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/app_components/activity/intent_lauchmode/01_single_top
[link_activity_flag_mode_02]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/app_components/activity/intent_lauchmode/02_new_task
[link_activity_flag_mode_03]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/app_components/activity/intent_lauchmode/03_clear_top
[link_activity_flag_mode_04]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/app_components/activity/intent_lauchmode/04_clear_task
[link_activity_fragment_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/app_components/fragment/list_fragment/01_simple
[link_activity_fragment_02]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/app_components/fragment/list_fragment/02_self_layout
[link_activity_preffragment_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/app_components/fragment/preference_fragment/01_basic
[link_activity_preffragment_02]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/app_components/fragment/preference_fragment/02_selfdeine_ListPreference_with_attr
[link_activity_preffragment_03]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/app_components/fragment/preference_fragment/03_selfdeine_ListPreference_plus_api
[link_intent_common_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/app_components/intent/common_intents/01_alarm
[link_intent_common_02]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/app_components/intent/common_intents/02_event
[link_intent_common_03]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/app_components/intent/common_intents/03_camera
[link_intent_common_04]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/app_components/intent/common_intents/04_contact
[link_intent_common_05]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/app_components/intent/common_intents/05_email
[link_intent_common_06]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/app_components/intent/common_intents/06_gallery
[link_intent_common_07]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/app_components/intent/common_intents/07_webview
[link_contentprovidr_01]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/app_components/contentprovider/01_basic/MyProvider
[link_contentprovidr_02]: https://github.com/wangkuiwu/android_applets/tree/master/api_guide/app_components/contentprovider/02_permission/MyProvider





