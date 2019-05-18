package mobile.senaigo.com.meuretrofit2.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import mobile.senaigo.com.meuretrofit2.R;
import mobile.senaigo.com.meuretrofit2.adapter.UserAdapter;
import mobile.senaigo.com.meuretrofit2.bootstrap.APIClient;
import mobile.senaigo.com.meuretrofit2.model.Address;
import mobile.senaigo.com.meuretrofit2.model.Company;
import mobile.senaigo.com.meuretrofit2.model.Geo;
import mobile.senaigo.com.meuretrofit2.model.User;
import mobile.senaigo.com.meuretrofit2.resource.UserResource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaPrincipal extends AppCompatActivity {
    UserResource apiUserResouce;

    private Integer posicao;
    User mapa;

    EditText txtUserId;
    EditText txtTitle;
    EditText txtBody;
    ListView listViewUser;
    List<User> listUser;
//    List<HashMap<String, String>> colecao =
//            new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        txtUserId = findViewById(R.id.txtUserId);
        txtTitle = findViewById(R.id.txtTitle);
        txtBody = findViewById(R.id.txtBody);
        listViewUser = findViewById(R.id.listViewUser);

        //tem o contexto da aplicação (application)
        //PASSO 4
        apiUserResouce = APIClient.getClient().create(UserResource.class);

        Call<List<User>> get = apiUserResouce.get();

        get.enqueue(new Callback<List<User>>() {

            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                listUser = response.body();
                confAdapter();
                listViewUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            mapa = listUser.get(i);
                            setPosicao(new Integer(i));
                            txtUserId.setText(mapa.getId());
                            txtBody.setText(mapa.getName());
                            txtTitle.setText(mapa.getUsername());
                        } catch (Exception e) {
                            AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                            alertDialog.setTitle("ERRO!");
                            alertDialog.setMessage(e.getMessage());
                            alertDialog.show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addUser(View view) {

        try {
            String userId = txtUserId.getText().toString();
            String body = txtBody.getText().toString();
            String title = txtTitle.getText().toString();

            if (userId == null || userId.isEmpty()) {
                throw new Exception("Não pode ser vazio");
            }

            if (body == null || body.isEmpty()) {
                throw new Exception("Não pode ser vazio");
            }

            if (title == null || title.isEmpty()) {
                throw new Exception("Não pode ser vazio");
            }

//            User user = new User(new Integer(userId), null, title, body);

            User user = new User();
            user.setAddress(new Address());
            user.setCompany(new Company());
            user.getAddress().setGeo(new Geo());
            user.setId(Integer.valueOf(userId));

            user.setName(title);
            user.setUsername(body);
            user.setEmail("email@email.com");
            user.setPhone("xx-xxxxx-xxxx");
            user.setWebsite("www.site.com");
            user.getAddress().setStreet("Endereço");
            user.getAddress().setSuite("Complemento");
            user.getAddress().setCity("Cidade");
            user.getAddress().setZipcode("Cep");
            user.getAddress().getGeo().setLat(000000000.1);
            user.getAddress().getGeo().setLng(000000000.1);
            user.getCompany().setName("Industria C.O");
            user.getCompany().setCatchPhrase("CP");
            user.getCompany().setBs("Bs");

            Call<User> post = apiUserResouce.post(user);
            post.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User u = response.body();
                    listUser.add(u);
                    confAdapter();
                    //limparCampos();

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                    alertDialog.setTitle("ERRO!");
                    alertDialog.setMessage(t.getMessage());
                    alertDialog.show();
                }
            });

        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("ERRO!");
            alertDialog.setMessage(e.getMessage());
            alertDialog.show();
        }

//        String username,data;
//        Integer id;
//        id = Integer.parseInt(txtId.getText().toString());
//        username = txtUserName.getText().toString();
//        data = txtData.getText().toString();
//
//
//        final User user = User.builder()
//                .id(id)
//                .userName(username)
//                .avatar(null)
//                .uuid(UUID.randomUUID().toString())
//                .data(data)
//                .build();
//
//        Call<User> post = apiUserResouce.post(user);
//        post.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                User u = response.body();
//                Toast.makeText(getApplicationContext(),
//                        u.toString(),
//                        Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Toast.makeText(getApplicationContext(),
//                        t.getMessage(),
//                        Toast.LENGTH_LONG).show();
//            }
//        });
//
//        Call<User> put = apiUserResouce.put(user);
//        put.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//
//            }
//        });
//
//        Call<Void> delete = apiUserResouce.delete(user);
//        delete.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//
//            }
//        });
    }

    public HashMap<String, String> converterMap(User user) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", user.getId() + "");
        map.put("id", user.getId() + "");
        map.put("title", user.getName());
        map.put("body", user.getUsername());
        return map;
    }

    public void confAdapter() {
        Log.i("teste2", listUser.size() + "");
        UserAdapter userAdapter = new UserAdapter(this, listUser);
        Log.i("teste2", userAdapter.getCount() + "");
        listViewUser.setAdapter(userAdapter);
    }

    public Integer getPosicao() {
        return posicao;
    }

    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }

    public void excluir(View view) {
//        try {
//            String userId = txtUserId.getText().toString();
//            String body = txtBody.getText().toString();
//            String title = txtTitle.getText().toString();
//
//            if (userId == null || userId.isEmpty()) {
//                throw new Exception("Não pode ser vazio");
//            }
//
//            if (body == null || body.isEmpty()) {
//                throw new Exception("Não pode ser vazio");
//            }
//
//            if (title == null || title.isEmpty()) {
//                throw new Exception("Não pode ser vazio");
//            }
//
//            User user = new User(new Integer(userId), null, title, body);
//
//            Call<User> post = apiUserResouce.post(user);
//            post.enqueue(new Callback<User>() {
//                @Override
//                public void onResponse(Call<User> call, Response<User> response) {
//                    User u = response.body();
//                    colecao.add(converterMap(u));
//                    confAdapter();
//                    //limparCampos();
//
//                }
//
//                @Override
//                public void onFailure(Call<User> call, Throwable t) {
//                    AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
//                    alertDialog.setTitle("ERRO!");
//                    alertDialog.setMessage(t.getMessage());
//                    alertDialog.show();
//                }
//            });
//
//        } catch (Exception e) {
//            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//            alertDialog.setTitle("ERRO!");
//            alertDialog.setMessage(e.getMessage());
//            alertDialog.show();
//        }
    }


    public void editar(View view) {
        try {
            String userId = txtUserId.getText().toString();
            String body = txtBody.getText().toString();
            String title = txtTitle.getText().toString();

            if (userId == null || userId.isEmpty()) {
                throw new Exception("Não pode ser vazio");
            }

            if (body == null || body.isEmpty()) {
                throw new Exception("Não pode ser vazio");
            }

            if (title == null || title.isEmpty()) {
                throw new Exception("Não pode ser vazio");
            }

            if (getPosicao() == null || getPosicao() < 0) {
                throw new Exception("Nenhum usuário foi selecionado!");
            }

            User user = new User();
            user.setAddress(new Address());
            user.setCompany(new Company());
            user.getAddress().setGeo(new Geo());
            user.setId(Integer.valueOf(userId));

            user.setName(title);
            user.setUsername(body);
            user.setEmail("email@email.com");
            user.setPhone("xx-xxxxx-xxxx");
            user.setWebsite("www.site.com");
            user.getAddress().setStreet("Endereço");
            user.getAddress().setSuite("Complemento");
            user.getAddress().setCity("Cidade");
            user.getAddress().setZipcode("Cep");
            user.getAddress().getGeo().setLat(000000000.1);
            user.getAddress().getGeo().setLng(000000000.1);
            user.getCompany().setName("Industria C.O");
            user.getCompany().setCatchPhrase("CP");
            user.getCompany().setBs("Bs");

            Call<User> put = apiUserResouce.put(user, user.getId());
            put.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User u = response.body();
                    listUser.set(getPosicao(), u);
                    confAdapter();
                    //limparCampos();

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                    alertDialog.setTitle("ERRO!");
                    alertDialog.setMessage(t.getMessage());
                    alertDialog.show();
                }
            });

        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("ERRO!");
            alertDialog.setMessage(e.getMessage());
            alertDialog.show();
        }
    }
}
