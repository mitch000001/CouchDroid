package org.mitchwork.couchdroid;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.couchbase.touchdb.TDServer;
import com.couchbase.touchdb.ektorp.TouchDBHttpClient;
import com.couchbase.touchdb.router.TDURLStreamHandlerFactory;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.DbAccessException;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.android.util.EktorpAsyncTask;
import org.ektorp.http.HttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity
{

    static {
        TDURLStreamHandlerFactory.registerSelfIgnoreError();
    }

    private static final String TAG = MainActivity.class.getSimpleName();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TDServer server = null;
        String filesDir = getFilesDir().getAbsolutePath();
        try {
            server = new TDServer(filesDir);
        } catch (IOException e) {
            Log.e(TAG, "Error starting TDServer", e);
        }
        HttpClient httpClient = new TouchDBHttpClient(server);
        CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
        CouchDbConnector db = dbInstance.createConnector("foo",true);
        Sofa sofa = new Sofa();
        sofa.setColor("green");
        db.create(sofa);
        String sofaId = sofa.getId();
        sofa = db.get(Sofa.class,sofaId);
        Log.d(TAG, "Sofa: " + sofa.getRevision());
        final SofaRepository repository = new SofaRepository(db);
        EktorpAsyncTask ektorpAsyncTask = new EktorpAsyncTask() {

            @Override
            protected void doInBackground() {
                Sofa redSofa = new Sofa();
                redSofa.setColor("red");
                repository.add(redSofa);
                List<Sofa> allSofas = repository.getAll();
                Log.d(TAG, "All sofas: " + allSofas.size() + " => " + allSofas);
                List<Sofa> redSofas = repository.findByColor("red");
                Log.d(TAG, "Red sofas: " + redSofas.size() + " => " + redSofas);
            }

            @Override
            protected void onSuccess() {
                Log.d(TAG, "SUCCESS!");
            }

            @Override
            protected void onDbAccessException(DbAccessException dbAccessException) {
                Log.e(TAG, "Caught DBAccessException: " + dbAccessException.toString(),dbAccessException);
            }

            @Override
            protected void onDocumentNotFound(DocumentNotFoundException documentNotFoundException) {
                Log.e(TAG, "Document not found: " + documentNotFoundException.getPath(),documentNotFoundException);
            }
        };
        ektorpAsyncTask.execute();

    }
}
