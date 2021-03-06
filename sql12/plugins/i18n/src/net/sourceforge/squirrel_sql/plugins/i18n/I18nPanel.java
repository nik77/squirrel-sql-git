package net.sourceforge.squirrel_sql.plugins.i18n;

import net.sourceforge.squirrel_sql.fw.util.StringManager;
import net.sourceforge.squirrel_sql.fw.util.StringManagerFactory;
import net.sourceforge.squirrel_sql.client.plugin.PluginResources;
import net.sourceforge.squirrel_sql.client.resources.SquirrelResources;

import javax.swing.*;
import java.awt.*;

public class I18nPanel extends JPanel
{
   private static final StringManager s_stringMgr = StringManagerFactory.getStringManager(I18nPanel.class);

   TranslatorsPanel pnlTranslators;

   JTextField txtNote;


   public I18nPanel(PluginResources resources)
   {
      pnlTranslators = new TranslatorsPanel(resources);


      GridBagConstraints gbc;

      setLayout(new GridBagLayout());
      gbc = new GridBagConstraints(0,0,1,1,1,1, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5,0,0,5),0,0);
      add(pnlTranslators, gbc);


      txtNote = new JTextField();
      txtNote.setText(s_stringMgr.getString("I18n.changeApplicationDefaultLocaleNote"));
      txtNote.setEditable(false);
      // i18n[I18n.changeApplicationDefaultLocaleNote=Example VM Parameters to change locale used by SQuirreL: -Duser.language=en -Duser.country=US]
      gbc = new GridBagConstraints(0,1,1,1,0,0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,0,5),0,0);
      add(txtNote, gbc);
   }
}
