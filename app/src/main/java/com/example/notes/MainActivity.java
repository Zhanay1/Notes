package com.example.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotes;
    private final ArrayList<Note> notes = new ArrayList<>();
    private NotesAdapter adapter;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        ActionBar actionBar = getSupportActionBar(); // Get control for ActionBar
        if (actionBar != null) {
            actionBar.hide(); // Hide ActionBar
        }

        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);// It will contain notes

        adapter = new NotesAdapter(notes);// RecyclerView needs adapter
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this)); // We give Layout Type(default Vertical, Horizontal, Box(n*n))
        getData();
        recyclerViewNotes.setAdapter(adapter); // Setting adapter
        adapter.setOnNoteClickListener(new NotesAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(int position) {
                Toast.makeText(MainActivity.this, "Position number: " + position, Toast.LENGTH_SHORT).show(); // Just show Message Position
            }

            @Override
            public void onLongClick(int position) {
                remove(position); // Long click to delete note element
            }
        });

        // ItemTouchHelper is necessary to detect Swipe by element
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                remove(viewHolder.getAdapterPosition()); // We delete swiped note
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewNotes);
    }

    private void remove(int position) {
        Note note = adapter.getNotes().get(position);
        viewModel.deleteNote(note);
    }

    public void onClickAddNote(View view) {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivity(intent);
    }

    private void getData() {
        LiveData<List<Note>> notesFromDB = viewModel.getNotes(); // Getting data from DataBase
        notesFromDB.observe(this, new Observer<List<Note>>() { // On DataBase change we will Update data on Screen
            @Override
            public void onChanged(@Nullable List<Note> notesFromLiveData) {
                adapter.setNotes(notesFromLiveData); // Set changes for the Notes
            }
        });
    }
}
