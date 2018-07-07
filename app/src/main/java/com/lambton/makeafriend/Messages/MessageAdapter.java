package com.lambton.makeafriend.Messages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lambton.makeafriend.MainActivity;
import com.danielcswain.makeafriend.R;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by ulternate on 3/08/2016.
 *
 * Custom Adapter extending the RecyclerView.Adapter class. This represents a single chat window and uses
 * the MessageViewHolder class to hold the individual message views which get their contents from the
 * mMessageObjects ArrayList
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private ArrayList<MessageObject> mMessageObjects;

    /**
     * Constructor for the MessageAdapter
     * @param messageObjects an ArrayList of MessageObject objects
     */
    public MessageAdapter(ArrayList<MessageObject> messageObjects){
        this.mMessageObjects = messageObjects;
    }

    /**
     * Get the ViewType of the item. This is used to determine whether to inflate the message_item_received or
     * message_item_sent layouts in onCreateViewHolder.
     *
     * @param position the position of the ItemView object in the mMessageObjects arrayList / RecyclerView.Adapter
     * @return 0 for a message that was sent the user and 1 if it was a received message.
     */
    @Override
    public int getItemViewType(int position) {
        MessageObject messageObject = mMessageObjects.get(position);
        if (messageObject.getFromUser()){
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Called when the RecyclerView needs a new ViewHolder of the given type to represent an item.
     * Uses the same MessageViewHolder class for both sent and received messages
     *
     * @param parent the parent view group
     * @param viewType the type of view to be represented in the ViewHolder. This is determined by getItemViewType
     *                 and the message type (either sent from the user or a received message)
     * @return a new MessageViewHolder with the given ViewType inflated
     */
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == 0) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_sent, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_received, parent, false);
        }

        return new MessageViewHolder(v);
    }

    /**
     * Displays the message data for the given position in the RecyclerView. Uses the MessageViewHolder to
     * get the correct references to the View Items
     * @param holder the MessageViewHolder representing a single RecyclerView item containing all the
     *               views for that item (i.e. the message body, username and avatar)
     * @param position the position of the item in the RecyclerView.Adapters data set.
     */
    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        // Get the messageObject from the ArrayList at the position specified
        MessageObject messageObject = mMessageObjects.get(position);

        // Set the TextViews from the ViewHolder with the appropriate data from the messageObject.
        holder.usernameTV.setText(messageObject.getUsername());

        // Handle the messageBody based upon the message Type
        if (Objects.equals(messageObject.getMessageContent(), MessageObject.MESSAGE_CONTENT_TEXT)) {
            // Show the messageBody as text and hide the imageView
            holder.messageBodyTV.setVisibility(View.VISIBLE);
            holder.messageBodyTV.setText(messageObject.getMessageBody());
            holder.messageBodyIV.setVisibility(View.GONE);
        } else if (Objects.equals(messageObject.getMessageContent(), MessageObject.MESSAGE_CONTENT_IMAGE)){
            // Show the messageBody as an image and hide the textView
            holder.messageBodyTV.setVisibility(View.GONE);
            holder.messageBodyIV.setVisibility(View.VISIBLE);
            // Get the byte[] from the messageBody to retrieve the image
            byte[] imageString = Base64.decode(messageObject.getMessageBody(), Base64.NO_WRAP);
            Bitmap bm = BitmapFactory.decodeByteArray(imageString, 0, imageString.length);
            holder.messageBodyIV.setImageBitmap(bm);
        }

        // Set the user avatar colour to match the user's chosen colour.
        try{
            holder.userAvatarIV.setColorFilter(Color.parseColor(messageObject.getAvatarColour()));
        } catch (IllegalArgumentException e){
            // Use the default pink colour if the messageObject.avatarColour property is not a valid Hex String
            holder.userAvatarIV.setColorFilter(ContextCompat.getColor(MainActivity.mainContext, R.color.md_pink_500));
        }

        // If not the first messageObject then check if the previous messageObject was from the same user.
        // if so, then don't display the user's avatar or username
        if (position > 0){
            String prevUsername = mMessageObjects.get(position - 1).getUsername();
            if (Objects.equals(prevUsername, messageObject.getUsername())){
                holder.userAvatarIV.setVisibility(View.GONE);
                holder.usernameTV.setVisibility(View.GONE);
            }
        }
    }

    /**
     * @return the total number of items in the data set held by the Adapter
     */
    @Override
    public int getItemCount() {
        if (mMessageObjects != null) {
            return mMessageObjects.size();
        } else {
            return 0;
        }
    }
}
