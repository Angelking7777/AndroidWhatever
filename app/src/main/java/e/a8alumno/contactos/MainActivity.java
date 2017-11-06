package e.a8alumno.contactos;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private TextView texto_usuario;
    private ImageView imagen_usuario;
    private String telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);


        this.textView = (TextView) this.findViewById(R.id.vista);
        this.texto_usuario = (TextView) this.findViewById(R.id.texto);
        this.imagen_usuario = (ImageView) this.findViewById(R.id.imagen);


        ContentResolver resolver = this.getContentResolver();//objeto para poder acceder a la obtención de todos los valores posibles de la lista de contactos a través de sus métodos

        Uri URI = ContactsContract.Data.CONTENT_URI;//con esto conseguimos la uri

        String[] projection = {//creamos un array de strings para guardar los campos como el id del contacto o el nombre
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.Data.PHOTO_URI,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };


        String where = ContactsContract.Data.MIMETYPE + "= '" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'";//campos necesarios para nuestro cursor pero que como no tienen valor le ponemos null.
        String[] args = null;
        String orden = null;
        Cursor resultado = resolver.query(URI, projection, where, args, orden);//projection es un array de cadenas, where es una cadena (objeto para almacenar todos los datos de nuestra lista)

        resultado.moveToPrevious();//nos ponemos al principio de la cadena, no sería tampoco necesario ponerlo pero por si acaso se pone
        while (resultado.moveToNext()) {//si hay datos devuelve un true
            this.textView.append("\n------------------\n");//salto de linea

            String id = resultado.getString(resultado.getColumnIndex(ContactsContract.Data.CONTACT_ID));//obtenemos el identificador pasándole el número de columna a través del método ContactsContract
            this.textView.append(id + "\n");

            String name = resultado.getString(resultado.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            this.textView.append(name + "\n");

            String foto = resultado.getString(resultado.getColumnIndex(ContactsContract.Data.PHOTO_URI));
            this.textView.append(foto + "\n");

            this.telefono = resultado.getString(resultado.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            this.textView.append(this.telefono + "\n");
            //para que la App nos funcione necesitamos aplicar persmisos para el acceso a la lista de contactos

        }

        resultado.moveToFirst();
        String id = resultado.getString(resultado.getColumnIndex(ContactsContract.Data.CONTACT_ID));//obtenemos el identificador pasándole el número de columna a través del método ContactsContract
        this.texto_usuario.append(id + "\n");

        String name = resultado.getString(resultado.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
        this.texto_usuario.append(name + "\n");

        String foto = resultado.getString(resultado.getColumnIndex(ContactsContract.Data.PHOTO_URI));
        this.texto_usuario.append(foto + "\n");

        if (foto != null) {
            Uri urifoto = Uri.parse(foto);
            this.imagen_usuario.setImageURI(urifoto);

        }}


    public void accionLlamarTelefono(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri uri_tel = Uri.parse("tel:" + this.telefono);
        intent.setData(uri_tel);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.startActivity(intent);

    }
}

