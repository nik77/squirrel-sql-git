package net.sourceforge.squirrel_sql.client.gui.db.aliascolor;

import net.sourceforge.squirrel_sql.client.gui.db.AliasFolder;
import net.sourceforge.squirrel_sql.client.gui.db.SQLAlias;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.Color;

public class AliasTreeColorer
{
   private Color _backgroundNonSelectionColor;
   private Color _backgroundSelectionColor;

   public AliasTreeColorer(JTree tree)
   {

      // For example the SubstanceDefaultTreeCellRenderer is not derived from DefaultTreeCellRenderer.
      // That is why these if clauses exist.
      // See also bug #1373.
      if (tree.getCellRenderer() instanceof DefaultTreeCellRenderer)
      {
         DefaultTreeCellRenderer defaultTreeCellRenderer = (DefaultTreeCellRenderer) tree.getCellRenderer();

         _backgroundNonSelectionColor = defaultTreeCellRenderer.getBackgroundNonSelectionColor();
         _backgroundSelectionColor = defaultTreeCellRenderer.getBackgroundSelectionColor();
      }
      else if(tree.getCellRenderer() instanceof JLabel)
      {
         JLabel labelCellRenderer = (JLabel) tree.getCellRenderer();
         _backgroundNonSelectionColor = labelCellRenderer.getBackground();
         _backgroundSelectionColor = labelCellRenderer.getBackground().darker();
      }
      else
      {
         JLabel labelCellRenderer = new JLabel();
         _backgroundNonSelectionColor = labelCellRenderer.getBackground();
         _backgroundSelectionColor = labelCellRenderer.getBackground().darker();
      }

   }

   public void colorAliasRendererComponent(DefaultTreeCellRenderer defaultTreeCellRenderer, DefaultMutableTreeNode node, JLabel cellRendererComp)
   {

      //cellRendererComp.setForeground(new JLabel().getForeground());

      defaultTreeCellRenderer.setBackgroundNonSelectionColor(_backgroundNonSelectionColor);
      defaultTreeCellRenderer.setBackgroundSelectionColor(_backgroundSelectionColor);

      if(node.getUserObject() instanceof SQLAlias)
      {
         SQLAlias sqlAlias = (SQLAlias) node.getUserObject();

         if(sqlAlias.getColorProperties().isOverrideAliasBackgroundColor())
         {
            colorRenderer(defaultTreeCellRenderer, sqlAlias.getColorProperties().getAliasBackgroundColorRgbValue());
         }
      }
      else if(node.getUserObject() instanceof AliasFolder)
      {
         AliasFolder aliasFolder = (AliasFolder) node.getUserObject();

         if(AliasFolder.NO_COLOR_RGB != aliasFolder.getColorRGB())
         {
            //cellRendererComp.setForeground(new Color(aliasFolder.getColorRGB()));
            colorRenderer(defaultTreeCellRenderer, aliasFolder.getColorRGB());

         }

      }
      else
      {
         // Though all default nodes with user objects of type String are removed in JTreeAliasesListImpl
         // during startup this method is still called with a user object of type String
         // That's why we don't throw an exception here.
         // throwUnknownUserObjectException(node);
      }
   }

   private void colorRenderer(DefaultTreeCellRenderer defaultTreeCellRenderer, int aliasBackgroundColorRgbValue)
   {
      defaultTreeCellRenderer.setBackgroundNonSelectionColor(new Color(aliasBackgroundColorRgbValue));
      defaultTreeCellRenderer.setBackgroundSelectionColor(new Color(aliasBackgroundColorRgbValue).darker());
   }
}
