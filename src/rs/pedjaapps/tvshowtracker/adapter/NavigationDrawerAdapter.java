/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rs.pedjaapps.tvshowtracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.model.NDItem;

/**
 * Adapter for the planet data used in our drawer menu,
 */
public class NavigationDrawerAdapter extends ArrayAdapter<NDItem>
{
    public NavigationDrawerAdapter(Context context, List<NDItem> objects)
    {
        super(context, 0, objects);
    }

    /**
     * Custom viewholder for our planet views.
     */
    public class ViewHolder
    {
        public TextView mTextView;
    }

	@Override
	public int getItemViewType(int position)
	{
		return getItem(position).type;
	}

	@Override
	public int getViewTypeCount()
	{
		return super.getViewTypeCount();
	}

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if(convertView == null)
        {
            LayoutInflater vi = LayoutInflater.from(parent.getContext());
            convertView = vi.inflate(R.layout.drawer_list_item, parent, false);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTextView.setText(getItem(position).title);
        return convertView;
    }
}
