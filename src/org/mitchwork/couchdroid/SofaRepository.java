package org.mitchwork.couchdroid;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;

import java.util.List;

/**
 * User: Michael Wagner
 * Date: 17.03.13
 * Time: 20:09
 */
@View( name = "all", map = "function(doc) { if (doc.type == 'Sofa') emit( null, doc._id )}")
public class SofaRepository extends CouchDbRepositorySupport<Sofa> {

    protected SofaRepository(CouchDbConnector db) {
        super(Sofa.class, db);
        initStandardDesignDocument();
    }

    @GenerateView
    public List<Sofa> findByColor(String color) {
        return queryView("by_color", color);
    }
}
