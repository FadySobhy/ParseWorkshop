/**
 * Your application code goes here<br>
 * This file was generated by <a href="https://www.codenameone.com/">Codename One</a> for the purpose
 * of building native mobile applications using Java.
 */


package userclasses;

import com.codename1.components.InfiniteProgress;
import com.codename1.components.ToastBar;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.list.MultiList;
import com.codename1.ui.util.Resources;
import com.parse4cn1.*;
import generated.StateMachineBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Your name here
 */
public class StateMachine extends StateMachineBase {
    public StateMachine(String resFile) {
        super(resFile);
        // do not modify, write code in initVars and initialize class members there,
        // the constructor might be invoked too late due to race conditions that might occur
    }

    /**
     * this method should be used to initialize variables instead of
     * the constructor/class scope to avoid race conditions
     */
    protected void initVars(Resources res) {
        Parse.initialize("http://docker19107-env-2854709.mircloud.host/parse", "myAppId", "myClientKey");
    }


    @Override
    protected void onLogin_LoginAction(Component c, ActionEvent event) {
        try {
            ParseUser user = ParseUser.create(findUsername().getText(), findPassword().getText());
            showBlocking();
            user.login();
            if (user.isAuthenticated()) {
                hideBlocking();
                ToastBar.showErrorMessage("Welcome Back");

                showForm("Home", null);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            hideBlocking();
            ToastBar.showErrorMessage(e.getMessage());
        }
    }

    @Override
    protected void onLogin_RegisterAction(Component c, ActionEvent event) {
        try {
            ParseUser user = ParseUser.create(findUsername().getText(), findPassword().getText());
            showBlocking();
            user.signUp();
            if (user.isAuthenticated()) {
                hideBlocking();
                ToastBar.showErrorMessage("Welcome");

                showForm("Home", null);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            hideBlocking();
            ToastBar.showErrorMessage(e.getMessage());
        }
        hideBlocking();
    }


    @Override
    protected void onHome_MyTodosAction(Component c, ActionEvent event) {
        try {
            HashMap<String, Object> item = (HashMap<String, Object>) ((MultiList) c).getSelectedItem();
            ParseObject object = (ParseObject) item.get("object");
            object.put("checked", !(Boolean.parseBoolean(item.get("emblem").toString())));
            showBlocking();
            object.save();
            hideBlocking();
        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }
        showForm("Home", null);
    }

    @Override
    protected boolean initListModelMyTodos(List cmp) {
        java.util.List<Map<String, Object>> mapList = new ArrayList<>();
        try {
            ParseQuery query = ParseQuery.getQuery("Task");
            query.whereEqualTo("user", ParseUser.getCurrent());
            showBlocking();
            java.util.List<ParseObject> list = query.find();
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> d = new HashMap<>();
                d.put("Line1", (list.get(i).getString("data")));

                d.put("emblem", list.get(i).getBoolean("checked"));
                d.put("object", list.get(i));
                mapList.add(d);
            }
            hideBlocking();
        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }
        cmp.setModel(new com.codename1.ui.list.DefaultListModel(mapList));
        return true;
    }


    @Override
    protected void onAdd_AddAction(Component c, ActionEvent event) {
        try {
            ParseObject task = ParseObject.create("Task");
            task.put("data", findTask().getText());
            task.put("user", ParseUser.getCurrent());
            task.put("checked", false);
            showBlocking();
            task.save();
            hideBlocking();
        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }
        showForm("Home", null);
    }


    Dialog blockDialog;

    void showBlocking() {

        blockDialog = new InfiniteProgress().showInifiniteBlocking();
        blockDialog.show();
    }

    void hideBlocking() {
        if (blockDialog != null)
            blockDialog.dispose();
    }


    @Override
    protected void onDelete_MyTodosAction(Component c, ActionEvent event) {
        try {
            HashMap<String, Object> item = (HashMap<String, Object>) ((MultiList) c).getSelectedItem();
            ParseObject object = (ParseObject) item.get("object");
            showBlocking();
            object.delete();
            hideBlocking();
        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }
        showForm("Delete", null);

    }

    @Override
    protected boolean allowBackTo(String formName) {
        if (formName.equals("Login"))
            return false;
        return super.allowBackTo(formName);
    }

    @Override
    protected void beforeHome(Form f) {
        f.getToolbar().addCommandToRightBar(new Command("Delete") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                showForm("Delete", null);
            }
        });
    }
}


