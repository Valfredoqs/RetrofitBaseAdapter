package mobile.senaigo.com.meuretrofit2.activity;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mobile.senaigo.com.meuretrofit2.R;
import mobile.senaigo.com.meuretrofit2.bootstrap.APIClient;
import mobile.senaigo.com.meuretrofit2.model.User;
import mobile.senaigo.com.meuretrofit2.resource.UserResource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaPrincipal extends AppCompatActivity {
    UserResource apiUserResouce;

    private Integer posicao;
    HashMap<String, String> mapa;

    EditText txtUserId;
    EditText txtTitle;
    EditText txtBody;
    ListView listViewUser;
    List<User> listUser;
    List<HashMap<String, String>> colecao =
            new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        txtUserId = findViewById(R.id.txtUserId);
        txtTitle = findViewById(R.id.txtTitle);
        txtBody = findViewById(R.id.txtBody);

        //tem o contexto da aplicação (application)
        //PASSO 4
        apiUserResouce = APIClient.getClient().create(UserResource.class);

        Call<List<User>> get = apiUserResouce.get();

        get.enqueue(new Callback<List<User>>() {

            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                listViewUser = findViewById(R.id.listViewUser);

                listUser = response.body();

                for (User u : listUser) {
                    colecao.add(converterMap(u));
                }

                confAdapter();
                listViewUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        mapa = colecao.get(i);
                        setPosicao(new Integer(i));
                        txtUserId.setText(mapa.get("userId"));
                        txtBody.setText(mapa.get("body"));
                        txtTitle.setText(mapa.get("title"));

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

            User user = new User(new Integer(userId), null, title, body);

            Call<User> post = apiUserResouce.post(user);
            post.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User u = response.body();
                   colecao.add(converterMap(u));
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
        map.put("userId", user.getUserId() + "");
        map.put("id", user.getId() + "");
        map.put("title", user.getTitle());
        map.put("body", user.getBody());
        return map;
    }

    public void confAdapter() {
        String[] from = {"id", "title"};
        int[] to = {R.id.txtUserId2, R.id.txtTitle2};

        SimpleAdapter simpleAdapter =
                new SimpleAdapter(
                        getApplicationContext(),
                        colecao,
                        R.layout.user,
                        from,
                        to);

        listViewUser.setAdapter(simpleAdapter);
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

            if (getPosicao() == null || getPosicao() < 0 ) {
                throw new Exception("Nenhum usuário foi selecionado!");
            }

            User user = new User(new Integer(userId), new Integer(mapa.get("id")), title, body);

            Call<User> put = apiUserResouce.put(user, user.getId());
            put.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User u = response.body();
                    colecao.set(getPosicao(), converterMap(u));
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
