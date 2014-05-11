package org.squirrelsql.session.sql;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.squirrelsql.services.FxmlHelper;
import org.squirrelsql.services.I18n;
import org.squirrelsql.session.sql.makeeditable.EditableCtrl;
import org.squirrelsql.table.EdittableTableController;
import org.squirrelsql.table.TableLoader;

public class ResultTabController
{
   private I18n _i18n = new I18n(getClass());

   private final Tab _containerTab;
   private EditableCtrl _editableCtrl;

   public ResultTabController(SQLResult sqlResult, String sql, SQLCancelTabCtrl sqlCancelTabCtrl)
   {

      String sqlAsTabText = sql.replaceAll("\n", " ");
      _containerTab = new Tab(sqlAsTabText);

      _containerTab.setContent(createContainerPane(sqlResult, sqlAsTabText, sqlCancelTabCtrl));

   }

   private BorderPane createContainerPane(SQLResult sqlResult, String sqlAsTabText, SQLCancelTabCtrl sqlCancelTabCtrl)
   {

      FxmlHelper<SQLResultHeaderView> headerFxmlHelper = new FxmlHelper(SQLResultHeaderView.class);


      String headertext;

      if(sqlResult.isMaxResultsReached())
      {
         headertext = _i18n.t("outputtab.max.results.reached", sqlResult.getMaxResults(), sqlAsTabText);
      }
      else
      {
         headertext = _i18n.t("outputtab.max.results.below", sqlResult.getResultTableLoader().size(), sqlAsTabText);
      }


      headerFxmlHelper.getView().lblHeader.setText(headertext);

      _editableCtrl = new EditableCtrl(sqlAsTabText, sqlResult);
      headerFxmlHelper.getView().resultToolBar.getItems().add(_editableCtrl.getEditButton());

      BorderPane bp = new BorderPane();

      //headerFxmlHelper.getView().lblHeader.setStyle("-fx-border-color: blue;");
      bp.setTop(headerFxmlHelper.getRegion());

      bp.setCenter(createContainerTabPane(sqlResult, sqlCancelTabCtrl));

      return bp;
   }

   private TabPane createContainerTabPane(SQLResult sqlResult, SQLCancelTabCtrl sqlCancelTabCtrl)
   {
      TabPane containerTabPane = new TabPane();
      containerTabPane.getTabs().add(createResultsTab(sqlResult));
      containerTabPane.getTabs().add(createResultMetaDataTab(sqlResult));

      if (null != sqlCancelTabCtrl)
      {
         containerTabPane.getTabs().add(sqlCancelTabCtrl.getTab());
      }

      return containerTabPane;
   }

   private Tab createResultMetaDataTab(SQLResult sqlResult)
   {
      return _createResultTableTab(sqlResult.getResultMetaDateTableLoader(), "outputtab.result.metadata");
   }

   private Tab createResultsTab(SQLResult sqlResult)
   {
      return _createResultTableTab(sqlResult.getResultTableLoader(), "outputtab.results");
   }


   private Tab _createResultTableTab(TableLoader tableLoader, String tabTextI18nKey)
   {
      Tab outputTab = new Tab(_i18n.t(tabTextI18nKey));

      TableView tv = new TableView();

      if (_editableCtrl.allowsEditing())
      {
         new EdittableTableController(tableLoader, tv);
      }
      else
      {

         tableLoader.load(tv);
      }

      outputTab.setContent(tv);
      outputTab.setClosable(false);

      return outputTab;
   }


   public Tab getTab()
   {
      return _containerTab;
   }
}
