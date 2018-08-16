package asquero.com.tech;


import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        String stuff[] = {"1","2","3","4","5","6","7","8","9","10"};

        recyclerView = findViewById(R.id.mainRecyclerView);

        myAdapter MyAdapter = new myAdapter(stuff);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(MyAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null){

            SendToStart();

        }
    }

    private void SendToStart() {

        Intent startIntent = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(startIntent);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.logOut){
            FirebaseAuth.getInstance().signOut();
            SendToStart();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        return true;
    }

    private class myAdapter extends RecyclerView.Adapter<GridHolder>{

        String stuff[];

        public myAdapter(String[] stuff) {
            this.stuff = stuff;
        }

        @NonNull
        @Override
        public GridHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.cardview_layout,parent,false);
            return new GridHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GridHolder holder, int position) {

            holder.textView.setText(stuff[position]);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,NewsDetailActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    //finish();
                }
            });

        }

        @Override
        public int getItemCount() {
            return stuff.length;
        }
    }

    private class GridHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private CardView cardView;

        public GridHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.cardText);
            cardView = itemView.findViewById(R.id.onClickableCard);
        }
    }

}