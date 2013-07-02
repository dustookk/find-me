package com.gdg.findme;

import java.util.List;

import com.gdg.findme.ContactsActivity;
import com.gdg.findme.R;
import com.gdg.findme.dao.TrustsDao;
import com.gdg.findme.vo.Contact;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class TrustActivity extends ListActivity implements OnClickListener {
	private List<Contact> trustsList;
	private MyArrayAdapter myArrayAdapter;
	private TrustsDao trustsDao;
	// "添加信任号码"按钮
	private Button bt_add;

	// 点添加信任号码弹出来的dialog
	private Dialog dialog;
	private EditText et_input_new_number;
	private Button bt_confirm_add;
	private Button bt_choose_from_contacts;
	private AlertDialog confirmDialog;
	// pressed Delete Button
	private View tempButtonView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trust);
		initView();
	}

	private void initView() {
		bt_add = (Button) findViewById(R.id.bt_add);
		bt_add.setOnClickListener(this);
		// initListView
		trustsDao = new TrustsDao(this);
		trustsList = trustsDao.getAll();
		myArrayAdapter = new MyArrayAdapter(this,
				R.layout.activity_contacts_item, trustsList);
		setListAdapter(myArrayAdapter);
		// initDialog
		dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_add_trust);
		et_input_new_number = (EditText) dialog
				.findViewById(R.id.et_input_new_number);
		bt_confirm_add = (Button) dialog.findViewById(R.id.bt_confirm_add);
		bt_choose_from_contacts = (Button) dialog
				.findViewById(R.id.bt_choose_from_contacts);

		bt_choose_from_contacts.setOnClickListener(this);
		bt_confirm_add.setOnClickListener(this);

		// initConfirmDialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("确定删除吗?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				int positionForView = getListView().getPositionForView(
						tempButtonView);
				tempButtonView = null;
				Contact contact = (Contact) getListView().getItemAtPosition(positionForView);
				if(trustsDao.remove(contact.getId())) {
					trustsList.remove(positionForView);
					myArrayAdapter.notifyDataSetChanged();
				}else {
					Toast.makeText(TrustActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// doNothing
			}
		});
		confirmDialog = builder.create();
	}

	@Override
	public void onClick(final View v) {
		if (v == bt_add) {// 点击了"添加信任号码"按钮
			et_input_new_number.setText("");
			dialog.show();
		} else if (v == bt_confirm_add) {// 确认添加
			String number = et_input_new_number.getText().toString().trim();
			if (number.matches("1[3,5,8]\\d{9}")) {
				Contact contact = new Contact(null, number);
				addContact(contact);
			} else {
				Toast.makeText(this, "请输入一个正确的手机号码", Toast.LENGTH_SHORT).show();
			}
		} else if (v == bt_choose_from_contacts) {// 从联系人中添加
			Intent intent = new Intent(this, ContactsActivity.class);
			startActivityForResult(intent, 0);
		} else { // 删除按钮
			tempButtonView = v;
			confirmDialog.show();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			String phoneRaw = data.getStringExtra("phone");
			String phone = phoneRaw.replace("-", "");
			String name = data.getStringExtra("name");
			Contact contact = new Contact(name, phone);
			addContact(contact);
		}
	}
	/**
	 * 完成添加联系人操作,包括界面的更新
	 * 
	 * @param contact
	 */
	private void addContact(Contact contact) {
		int id=(int) trustsDao.add(contact);
		if (id!=-1) {
			contact.setId(id);
			trustsList.add(contact);
			myArrayAdapter.notifyDataSetChanged();
			Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
			dialog.dismiss();
		} else {
			Toast.makeText(this, "该号码已经存在", Toast.LENGTH_SHORT).show();
		}
	}

	class MyArrayAdapter extends ArrayAdapter<Contact> {
		private List<Contact> adapterList;
		private int itemResourceId;

		public MyArrayAdapter(Context context, int itemResourceId,
				List<Contact> adapterList) {
			super(context, itemResourceId, adapterList);
			this.itemResourceId = itemResourceId;
			this.adapterList = adapterList;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder viewHolder;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(itemResourceId, null);
				viewHolder = new ViewHolder();
				viewHolder.tv_phone = (TextView) view
						.findViewById(R.id.tv_phone);
				viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
				viewHolder.bt_delete_trust = (Button) view
						.findViewById(R.id.bt_delete_trust);
				viewHolder.bt_delete_trust
						.setOnClickListener(TrustActivity.this);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			viewHolder = (ViewHolder) view.getTag();

			viewHolder.tv_name.setText(adapterList.get(position).getName());
			viewHolder.tv_phone.setText(adapterList.get(position).getNumber());
			return view;
		}

		@Override
		public Contact getItem(int position) {
			return super.getItem(position);
		}

		class ViewHolder {
			TextView tv_phone;
			TextView tv_name;
			Button bt_delete_trust;
		}
	}

}