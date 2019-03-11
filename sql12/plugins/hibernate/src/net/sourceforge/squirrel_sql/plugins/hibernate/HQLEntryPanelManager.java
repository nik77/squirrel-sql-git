package net.sourceforge.squirrel_sql.plugins.hibernate;

import net.sourceforge.squirrel_sql.client.session.EntryPanelManager;
import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.client.session.ISyntaxHighlightTokenMatcherFactory;
import net.sourceforge.squirrel_sql.client.session.ISyntaxHighlightTokenMatcher;
import net.sourceforge.squirrel_sql.client.gui.session.ToolsPopupController;
import net.sourceforge.squirrel_sql.fw.util.StringManager;
import net.sourceforge.squirrel_sql.fw.util.StringManagerFactory;
import net.sourceforge.squirrel_sql.plugins.hibernate.completion.HQLCompleteCodeAction;
import net.sourceforge.squirrel_sql.plugins.hibernate.util.HibernateSQLUtil;
import net.sourceforge.squirrel_sql.plugins.hibernate.util.HqlQueryErrorUtil;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;

public class HQLEntryPanelManager extends EntryPanelManager
{

   private static final StringManager s_stringMgr = StringManagerFactory.getStringManager(HQLEntryPanelManager.class);

   private HqlSyntaxHighlightTokenMatcherProxy _hqlSyntaxHighlightTokenMatcherProxy = new HqlSyntaxHighlightTokenMatcherProxy();
   private HibernatePluginResources _resources;
   private IHibernateConnectionProvider _connectionProvider;
   private ToolsPopupController _toolsPopupController;


   public HQLEntryPanelManager(final ISession session, HibernatePluginResources resources, IHibernateConnectionProvider connectionProvider)
   {
      super(session);
      _connectionProvider = connectionProvider;

      ToolsPopupAccessorProxy tpap = new ToolsPopupAccessorProxy();

      init(createSyntaxHighlightTokenMatcherFactory(), tpap);

      _resources = resources;

      initToolsPopUp();
      tpap.apply(this);
      initCodeCompletion();
      initBookmarks();


      // i18n[HQLEntryPanelManager.quoteHQL=Quote HQL]
      String strQuote = s_stringMgr.getString("HQLEntryPanelManager.quoteHQL");
      AbstractAction quoteHql = new AbstractAction(strQuote)
      {
         public void actionPerformed(ActionEvent e)
         {
            onQuoteHQL();
         }
      };
      quoteHql.putValue(Action.SHORT_DESCRIPTION, strQuote);
      addToSQLEntryAreaMenu(quoteHql, "quote");

      // i18n[HQLEntryPanelManager.quoteHQLsb=Quote HQL as StingBuffer]
      String strQuoteSb = s_stringMgr.getString("HQLEntryPanelManager.quoteHQLsb");
      AbstractAction quoteSbHql = new AbstractAction(strQuoteSb)
      {
         public void actionPerformed(ActionEvent e)
         {
            onQuoteHQLSb();
         }
      };
      quoteSbHql.putValue(Action.SHORT_DESCRIPTION, strQuoteSb);
      addToSQLEntryAreaMenu(quoteSbHql, "quotesb");

      // i18n[HQLEntryPanelManager.unquoteHQL=Unquote HQL]
      String strUnquote = s_stringMgr.getString("HQLEntryPanelManager.unquoteHQL");
      AbstractAction unquoteHql = new AbstractAction(strUnquote)
      {
         public void actionPerformed(ActionEvent e)
         {
            onUnquoteHQL();
         }
      };
      unquoteHql.putValue(Action.SHORT_DESCRIPTION, strUnquote);
      addToSQLEntryAreaMenu(unquoteHql, "unquote");

      // i18n[HQLEntryPanelManager.escapeDate=Escape date]
      String strEscapeDate = s_stringMgr.getString("HQLEntryPanelManager.escapeDate");
      AbstractAction escapeDate = new AbstractAction(strEscapeDate)
      {
         public void actionPerformed(ActionEvent e)
         {
            onEscapeDate();
         }
      };
      escapeDate.putValue(Action.SHORT_DESCRIPTION, strEscapeDate);
      addToSQLEntryAreaMenu(escapeDate, "date");

      String strCopySqlToClip = s_stringMgr.getString("HQLEntryPanelManager.copySqlToClip");
      AbstractAction copySqlToClip = new AbstractAction(strCopySqlToClip)
      {
         public void actionPerformed(ActionEvent e)
         {
            onCopySqlToClip(session);
         }
      };
      copySqlToClip.putValue(Action.SHORT_DESCRIPTION, strCopySqlToClip);
      addToSQLEntryAreaMenu(copySqlToClip, "sqltoclip");
   }

   private void onCopySqlToClip(ISession session)
   {
      try
      {
         HibernateSQLUtil.copySqlToClipboard(_connectionProvider.getHibernateConnection(), getEntryPanel().getSQLToBeExecuted(), session);
         session.showMessage(s_stringMgr.getString("HQLEntryPanelManager.copySqlToClipSucceeded"));
      }
      catch (Throwable t)
      {
         HqlQueryErrorUtil.handleHqlQueryError(t, session, true);
      }
   }

   private void onEscapeDate()
   {
      String str = EditExtrasAccessor.escapeDate(getEntryPanel());

      if(null != str)
      {
         getEntryPanel().replaceSelection(str);
      }
   }


   private void initBookmarks()
   {
      HQLBookmarksAction hba = new HQLBookmarksAction(getSession().getApplication(), _resources, getEntryPanel());
      JMenuItem item = addToSQLEntryAreaMenu(hba, "bookmarkselect");
      _resources.configureMenuItem(hba, item);
      registerKeyboardAction(hba, _resources.getKeyStroke(hba));
   }

   private void initToolsPopUp()
   {
      _toolsPopupController = new ToolsPopupController(getSession(), getEntryPanel());
      HQLToolsPopUpAction htp = new HQLToolsPopUpAction(_resources, _toolsPopupController, getSession().getApplication());
      JMenuItem item = addToSQLEntryAreaMenu(htp, null);
      _resources.configureMenuItem(htp, item);
      registerKeyboardAction(htp, _resources.getKeyStroke(htp));
   }


   private void initCodeCompletion()
   {
      HQLCompleteCodeAction hcca = new HQLCompleteCodeAction(getSession().getApplication(), _resources, this, _connectionProvider, _hqlSyntaxHighlightTokenMatcherProxy, getSession());
      JMenuItem item = addToSQLEntryAreaMenu(hcca, "complete");
      _resources.configureMenuItem(hcca, item);
      registerKeyboardAction(hcca, _resources.getKeyStroke(hcca));
   }


   private ISyntaxHighlightTokenMatcherFactory createSyntaxHighlightTokenMatcherFactory()
   {
      return new ISyntaxHighlightTokenMatcherFactory()
      {
         public ISyntaxHighlightTokenMatcher getSyntaxHighlightTokenMatcher(ISession sess, JTextComponent editorPane)
         {
            _hqlSyntaxHighlightTokenMatcherProxy.setEditorPane(editorPane);
            return _hqlSyntaxHighlightTokenMatcherProxy;
         }
      };

   }


   private void onUnquoteHQL()
   {
      EditExtrasAccessor.unquoteHQL(getEntryPanel());
   }

   private void onQuoteHQLSb()
   {
      EditExtrasAccessor.quoteHQLSb(getEntryPanel());
   }

   private void onQuoteHQL()
   {
      EditExtrasAccessor.quoteHQL(getEntryPanel());
   }


   public JMenuItem addToSQLEntryAreaMenu(Action action, String toolsPopupSelectionString)
   {
      if(null != toolsPopupSelectionString)
      {
         _toolsPopupController.addAction(toolsPopupSelectionString, action);
      }

      return getEntryPanel().addToSQLEntryAreaMenu(action);
   }

   public void registerKeyboardAction(Action action, KeyStroke keyStroke)
   {
      JComponent comp = getEntryPanel().getTextComponent();
      comp.registerKeyboardAction(action, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
   }
}
