/**
 * GUI 包。
 * <p>
 * 该包中有许多本应该是 package 级别的类，因为需要使用 WindowBuilder 进行设计，故而讲权限提升至了 public，这些类的权限虽然是
 * public ，但本身却并不应该被外部包引用。
 * 
 * <p>
 * 以下列出所有的本应该是 package 级别，却提升至 public 级别的包，这些包不应该被外部包引用——就算将来将这些包的 public
 * 权限收回，也不会进行通知。
 * 
 * <p>
 * <table cellpadding="0" cellspacing="3" border="0" style="text-align: left;
 * width: 50%;">
 * <caption><b> 专用工具包名一览：</b></caption> <tbody>
 * <tr>
 * <th style="vertical-align: top; background-color: rgb(204, 204, 255);
 * text-align: center; ">类名<br>
 * </th>
 * </tr>
 * <tr>
 * <td style="vertical-align: middle; width = 66%">
 * <code> {@link com.dwarfeng.projwiz.core.view.gui.ProjWizFrame}<br></code></td>
 * </tr>
 * <tr>
 * <td style="vertical-align: middle; background-color: rgb(238, 238, 255);">
 * <code> {@link com.dwarfeng.projwiz.core.view.gui.ProjWizMenuBar}<br></code></td>
 * </tr>
 * <tr>
 * <td style="vertical-align: middle; width = 66%">
 * <code> {@link com.dwarfeng.projwiz.core.view.gui.ProjWizDialog}<br></code></td>
 * </tr>
 * <tr>
 * <td style="vertical-align: middle; background-color: rgb(238, 238, 255);">
 * <code> {@link com.dwarfeng.projwiz.core.view.gui.ProjWizMenu}<br></code></td>
 * </tr>
 * <tr>
 * <td style="vertical-align: middle; width = 66%">
 * <code> {@link com.dwarfeng.projwiz.core.view.gui.ProjWizPanel}<br></code></td>
 * </tr>
 * <tr>
 * <td style="vertical-align: middle; background-color: rgb(238, 238, 255);">
 * <code> {@link com.dwarfeng.projwiz.core.view.gui.MfMenuBar_01}<br></code></td>
 * </tr>
 * <tr>
 * <td style="vertical-align: middle; width = 66%">
 * <code> {@link com.dwarfeng.projwiz.core.view.gui.MfMenu_01}<br></code></td>
 * </tr>
 * <tr>
 * <td style="vertical-align: middle; background-color: rgb(238, 238, 255);">
 * <code> {@link com.dwarfeng.projwiz.core.view.gui.MfMenu_02}<br></code></td>
 * </tr>
 * </tbody>
 * </table>
 * </p>
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
package com.dwarfeng.projwiz.core.view.gui;