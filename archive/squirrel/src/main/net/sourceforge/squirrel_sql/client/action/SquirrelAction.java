package net.sourceforge.squirrel_sql.client.action;
/*
 * Copyright (C) 2001 Colin Bell
 * colbell@users.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
import net.sourceforge.squirrel_sql.fw.gui.action.BaseAction;
import net.sourceforge.squirrel_sql.fw.util.Resources;

import net.sourceforge.squirrel_sql.client.IApplication;

public abstract class SquirrelAction extends BaseAction {
    private IApplication _app;

    protected SquirrelAction(IApplication app) {
        this(app, app.getResources());
    }

    protected SquirrelAction(IApplication app, Resources rsrc) {
        super();
        if (app == null) {
            throw new IllegalArgumentException("Null IApplication passed");
        }
        if (rsrc == null) {
            throw new IllegalArgumentException("No Resources object in IApplication");
        }


        _app = app;
        rsrc.setupAction(this);
    }

    protected IApplication getApplication() {
        return _app;
    }
}
