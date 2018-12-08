package xyz.vinayak.scrumup;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class TasksFragment extends Fragment {

    Context context;
    RecyclerView rv;
    public static ImageView ivNoNote;
    ArrayList<Todo> todoArrayList;
    TodoAdapter todoAdapter;
    CoordinatorLayout coordinatorLayout;
    NoteDb noteDb;
    public static final int ADDNOTE_ACTIVITY_REQUEST_CODE = 0012300;
    public static final int  EDITNOTE_ACTIVITY_REQUEST_CODE = 0032100;
    SharedPreferences prefs = null;
    FloatingActionButton fab;

    public TasksFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);
        context = rootView.getContext();

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        MyReceiver mReceiver = new MyReceiver (getActivity());
        context.registerReceiver(mReceiver, filter);

        noteDb = new NoteDb(getActivity());

        todoArrayList = noteDb.getAllTodos();

        coordinatorLayout = rootView.findViewById(R.id.coordinatorLayoutMain);
        rv = rootView.findViewById(R.id.recyclerView);
        ivNoNote = rootView.findViewById(R.id.ivNoNotes);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (todoArrayList.size() > 0) {
            ivNoNote.setVisibility(View.GONE);
        }


        fab = rootView.findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddNewNoteActivity.class);
                startActivityForResult(intent, ADDNOTE_ACTIVITY_REQUEST_CODE);
            }
        });

        todoAdapter = new TodoAdapter(context, todoArrayList, noteDb);

        rv.setAdapter(todoAdapter);


        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static TasksFragment newInstance(String text) {

        TasksFragment tf = new TasksFragment();
//        Bundle b = new Bundle();
//        b.putString("msg", text);
//
//        tf.setArguments(b);

        return tf;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADDNOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            todoArrayList.add((Todo) data.getSerializableExtra("newtodo"));
            Snackbar.make(coordinatorLayout, "Note Saved", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            recyclerViewUpdateData();
        }
        else if (requestCode == EDITNOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            int index = data.getIntExtra("position",-1);
            Todo newTodo = (Todo) data.getSerializableExtra("newtodo");
            noteDb.getWritableDatabase().delete(Constants.TABLE_NAME, Constants.COLUMN_ID + " = ?", new String[]{String.valueOf(todoArrayList.get(index).getId())});
            todoArrayList.remove(index);
            todoArrayList.add(index, newTodo);
            recyclerViewUpdateData();
            //Handle here deletion of Todo and addition of new todo in database after editing is Done

            Snackbar.make(coordinatorLayout, "Note Saved with Changes", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();

        }
    }

    private void recyclerViewUpdateData() {
        todoAdapter.notifyDataSetChanged();
        if (todoArrayList.size() > 0) {
            ivNoNote.setVisibility(View.GONE);
        } else
            ivNoNote.setVisibility(View.VISIBLE);

    }


    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            onResume();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!getUserVisibleHint())
        {
            return;
        }

        for (Todo todo : todoArrayList){
            noteDb.insertNote(todo);
        }
    }

}