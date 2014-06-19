
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



<a name="anchor1_2"></a>
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
## 使用Fragment打造灵活的UI

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




