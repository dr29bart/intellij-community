package org.jetbrains.idea.maven.project;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Vladislav.Kaznacheev
 */
public class MavenImportingSettingsForm {

  private JPanel panel;
  private JCheckBox myModuleDirCheckBox;
  private TextFieldWithBrowseButton myModuleDirControl;
  private JCheckBox myUseExhaustiveSearchCheckBox;
  private JCheckBox myAutoSyncCheckBox;
  private JCheckBox myCreateModulesForAggregators;
  private JCheckBox myCreateGroupsCheckBox;
  private JCheckBox myUseMavenOutputCheckBox;
  private JCheckBox myUpdateFoldersOnImportCheckBox;
  private JCheckBox myResolveInBackgroundCheckBox;

  public MavenImportingSettingsForm() {
    this(false);
  }

  public MavenImportingSettingsForm(boolean isImportStep) {

    ActionListener listener = new ActionListener() {
      public void actionPerformed(final ActionEvent e) {
        enableControls();
      }
    };
    myModuleDirCheckBox.addActionListener(listener);

    myModuleDirControl.addBrowseFolderListener(ProjectBundle.message("maven.import.title.module.dir"), "", null,
                                               new FileChooserDescriptor(false, true, false, false, false, false));

    if(isImportStep){
      myCreateGroupsCheckBox.setVisible(false);
      myUseMavenOutputCheckBox.setVisible(false);
      myUpdateFoldersOnImportCheckBox.setVisible(false);
      myResolveInBackgroundCheckBox.setVisible(false);
    } else {
      myUseExhaustiveSearchCheckBox.setVisible(false);
    }
  }

  private void enableControls() {
    final boolean dedicated = myModuleDirCheckBox.isSelected();
    myModuleDirControl.setEnabled(dedicated);
    if (dedicated && StringUtil.isEmptyOrSpaces(myModuleDirControl.getText())) {
      myModuleDirControl.setText(FileUtil.toSystemDependentName(getDefaultModuleDir()));
    }
  }

  public String getDefaultModuleDir() {
    return "";
  }

  public JComponent createComponent() {
    return panel;
  }

  public void getData(MavenImportingSettings data) {
    data.setDedicatedModuleDir(myModuleDirCheckBox.isSelected() ? myModuleDirControl.getText() : "");
    data.setCreateModuleGroups(myCreateGroupsCheckBox.isSelected());
    data.setAutoSync(myAutoSyncCheckBox.isSelected());
    data.setCreateModulesForAggregators(myCreateModulesForAggregators.isSelected());
    data.setLookForNested(myUseExhaustiveSearchCheckBox.isSelected());
    data.setUseMavenOutput(myUseMavenOutputCheckBox.isSelected());
    data.setUpdateFoldersOnImport(myUpdateFoldersOnImportCheckBox.isSelected());
    data.setResolveInBackground(myResolveInBackgroundCheckBox.isSelected());
  }

  public void setData(final MavenImportingSettings data) {
    myModuleDirCheckBox.setSelected(!StringUtil.isEmptyOrSpaces(data.getDedicatedModuleDir()));
    myModuleDirControl.setText(data.getDedicatedModuleDir());

    myAutoSyncCheckBox.setSelected(data.isAutoSync());
    myCreateModulesForAggregators.setSelected(data.isCreateModulesForAggregators());
    myCreateGroupsCheckBox.setSelected(data.isCreateModuleGroups());
    myUseExhaustiveSearchCheckBox.setSelected(data.isLookForNested());
    myUseMavenOutputCheckBox.setSelected(data.isUseMavenOutput());
    myUpdateFoldersOnImportCheckBox.setSelected(data.isUpdateFoldersOnImport());
    myResolveInBackgroundCheckBox.setSelected(data.isResolveInBackground());

    enableControls();
  }

  public boolean isModified(MavenImportingSettings settings) {
    MavenImportingSettings formData = new MavenImportingSettings();
    getData(formData);
    return !formData.equals(settings);
  }
}
