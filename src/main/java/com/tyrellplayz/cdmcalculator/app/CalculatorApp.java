package com.tyrellplayz.cdmcalculator.app;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Dialog;
import com.mrcrayfish.device.api.app.Icons;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.ComboBox;
import com.mrcrayfish.device.api.app.component.Label;
import com.mrcrayfish.device.api.app.component.Text;
import com.mrcrayfish.device.api.app.component.TextArea;
import com.mrcrayfish.device.api.app.component.TextField;
import com.mrcrayfish.device.api.app.listener.ClickListener;
import com.mrcrayfish.device.core.Laptop;
import com.tyrellplayz.cdmcalculator.Reference;

import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class CalculatorApp extends Application{

	private Button button_info;
	
	private Label label_hoverInfo;
	private ComboBox.List<String> comboBox_modePicker;
	
	private TextField textField_resultNor;
	private TextField textField_resultAdv;

	// Normal Mode
	private Button button_numberZero;
	
	private Button button_add;
	private Button button_minus;
	private Button button_times;
	private Button button_divide;
	private Button button_equals;
	private Button button_dot;
	
	private Button button_clear;
	private Button button_backspace;
	
	// Advance Mode
	private Button button_square;
	private Button button_sqrt;
	private Button button_abs;
	private Button button_sin;
	private Button button_cos;
	private Button button_tan;
	private Button button_log;
	private Button button_modulo;
	private Button button_openBracket;
	private Button button_closedBracket;

	// Layouts
	private Layout layout_normal;
	private Layout layout_advance;
	private Layout layout_info;
	private Layout layout_history;

	// History
	private TextArea textArea_history;
	private Button button_clearHistory;
	private Button button_refreshHistory;
	private Button button_saveHistory;
	private Button button_loadHistory;

	private List<String> history = new ArrayList<>();
	public void setHistory(List<String> history) {this.history = history;}
	public List<String> getHistory() {return history;}
	public void addHistory(String calculation) {history.add(calculation);}

	private Button normal_mode;
	private Button advance_mode;
	private Button history_mode;

	private Button button_back;
	
	private Label label_author;
	private Label label_twitter;
	private Text text_exp4j;
	
	private String result;
	/**
	 * n = normal, a = advance
	 */
	private String mode = "n";

	@Override
	public void init(NBTTagCompound tag) {
		
		layout_normal = new Layout(100, 120);
		layout_advance = new Layout(197, 120);
		layout_info = new Layout(230, 100);
		layout_history = new Layout(197, 120);
		
		layoutNormal();
		layoutAdvance();
		layoutInfo();
		layoutHistory();
		
		button_info = new Button(83, 2, Icons.INFO);
		button_info.setToolTip("Info", "Info about the app");
		button_info.setClickListener((mouseX, mouseY, mouseButton) -> {
			this.setCurrentLayout(layout_info);
		});
		layout_normal.addComponent(button_info);
		layout_advance.addComponent(button_info);
		layout_history.addComponent(button_info);

		normal_mode = new Button(2, 2, Icons.ONLINE);
		normal_mode.setToolTip("Normal","For every day calculations");
		normal_mode.setClickListener((mouseX, mouseY, mouseButton) -> {
			this.setCurrentLayout(layout_normal);
			textField_resultNor.clear();
			textField_resultAdv.clear();
			mode = "n";
        });

		advance_mode = new Button(20, 2, Icons.LIVE);
		advance_mode.setToolTip("Advance","For advance calculations");
		advance_mode.setClickListener((mouseX, mouseY, mouseButton) -> {
			this.setCurrentLayout(layout_advance);
			textField_resultNor.clear();
			textField_resultAdv.clear();
			mode = "a";
		});

		history_mode = new Button(38, 2, Icons.BOOK_CLOSED);
		history_mode.setToolTip("History","View last calculations");
		history_mode.setClickListener((mouseX, mouseY, mouseButton) -> {
			this.setCurrentLayout(layout_history);
			textField_resultNor.clear();
			textField_resultAdv.clear();
			mode = "h";
		});

		layout_normal.addComponent(normal_mode);
		layout_normal.addComponent(advance_mode);
		layout_normal.addComponent(history_mode);
		layout_advance.addComponent(normal_mode);
		layout_advance.addComponent(advance_mode);
		layout_advance.addComponent(history_mode);
		layout_history.addComponent(normal_mode);
		layout_history.addComponent(advance_mode);
		layout_history.addComponent(history_mode);

		this.setCurrentLayout(layout_normal);
	}
	
	@Override
	public void onClose() {
		history.clear();
		super.onClose();
	}
	
	public void layoutNormal() {
		layout_normal.setBackground((gui, mc, x, y, width, height, mouseX, mouseY, windowActive) -> {
			Gui.drawRect(x, y, x + width, y + 20, Laptop.getSystem().getSettings().getColorScheme().getBackgroundColor());
			Gui.drawRect(x, y + 20, x + width, y + 21, Color.DARK_GRAY.getRGB());
			//mc.fontRenderer.drawString("CalculatorPlus", x + 22, y + 6, Color.WHITE.getRGB(), true);
		});
		
		textField_resultNor = new TextField(2, 25, 96);
		textField_resultNor.setEditable(false);
		layout_normal.addComponent(textField_resultNor);
		
		for(int i = 0; i < 9; i++){
			int posX = 2 + (i % 3) * 19;
			int posY = 45 + (i / 3) * 19;
			Button button = new Button(posX, posY, Integer.toString(i + 1));
			button.setSize(16, 16);
			addNumberClickListener(button, i + 1);
			layout_normal.addComponent(button);
			layout_advance.addComponent(button);
		}
		
		//Row 4
		
		button_numberZero = new Button(21, 102, "0");
		button_numberZero.setSize(16, 16);
		addNumberClickListener(button_numberZero, 0);
		layout_normal.addComponent(button_numberZero);
		layout_advance.addComponent(button_numberZero);
		
		//R1
		button_add = new Button(62, 45, "+");
		button_add.setSize(16, 16);
		button_add.setToolTip("Plus", "Add");
		button_add.setClickListener((mouseX, mouseY, mouseButton) -> {
			if(mouseButton == 0) {
				writeText("+");
			}
		});
		layout_normal.addComponent(button_add);
		layout_advance.addComponent(button_add);
		
		button_clear = new Button(82, 45, "C");
		button_clear.setSize(16, 16);
		button_clear.setToolTip("Clear", "Clear the textfield");
		button_clear.setClickListener((mouseX, mouseY, mouseButton) -> {
			if(mouseButton == 0) {
				textField_resultNor.clear();
				textField_resultAdv.clear();
			}
		});
		layout_normal.addComponent(button_clear);
		layout_advance.addComponent(button_clear);
		
		button_minus = new Button(62, 64, "-");
		button_minus.setSize(16, 16);
		button_minus.setToolTip("Takeaway", "Minus");
		button_minus.setClickListener((mouseX, mouseY, mouseButton) -> {
			if(mouseButton == 0) {
				writeText("-");
			}
		});
		layout_normal.addComponent(button_minus);
		layout_advance.addComponent(button_minus);
		
		button_backspace = new Button(82, 64, "<");
		button_backspace.setSize(16, 16);
		button_backspace.setToolTip("Backspace", "Remove last number");
		button_backspace.setClickListener((mouseX, mouseY, mouseButton) -> {
			if(mouseButton == 0) {
				if(textField_resultNor.getText().equals("ERROR") || textField_resultAdv.getText().equals("ERROR")) {
					textField_resultNor.clear();
					textField_resultAdv.clear();
				}else {
					textField_resultNor.performBackspace();
					textField_resultAdv.performBackspace();
				}
			}
		});
		layout_normal.addComponent(button_backspace);
		layout_advance.addComponent(button_backspace);
		
		button_times = new Button(62, 83, "*");
		button_times.setSize(16, 16);
		button_times.setToolTip("Times", "Multiply");
		button_times.setClickListener((mouseX, mouseY, mouseButton) -> {
			if(mouseButton == 0) {
				writeText("*");
			}
		});
		layout_normal.addComponent(button_times);
		layout_advance.addComponent(button_times);
		
		button_dot = new Button(82, 83, ".");
		button_dot.setSize(16, 16);
		button_dot.setToolTip("Dot", "Point");
		button_dot.setClickListener((mouseX, mouseY, mouseButton) -> {
			if(mouseButton == 0) {
				writeText(".");
			}
		});
		layout_normal.addComponent(button_dot);
		layout_advance.addComponent(button_dot);
		
		button_divide = new Button(62, 102, "/");
		button_divide.setSize(16, 16);
		button_divide.setToolTip("Divide", "Division");
		button_divide.setClickListener((mouseX, mouseY, mouseButton) -> {
			if(mouseButton == 0) {
				writeText("/");
			}
		});
		layout_normal.addComponent(button_divide);
		layout_advance.addComponent(button_divide);
		
		/*
		 * Equals
		 */
		button_equals = new Button(82, 102, "=");
		button_equals.setSize(16, 16);
		button_equals.setToolTip("Equal", "Calculate");
		button_equals.setClickListener((mouseX, mouseY, mouseButton) -> {
			if(mouseButton == 0) {
				String calculation = textField_resultNor.getText();
				try {
					Expression e = new ExpressionBuilder(calculation).build();
					result = String.valueOf(e.evaluate());
					addHistory(calculation + " = " + result);
					setText(result, textField_resultNor);
					setText(result, textField_resultAdv);
				}catch (Exception e) {
					setText("ERROR", textField_resultNor);
					setText("ERROR", textField_resultAdv);
				}
			}
		});
		layout_normal.addComponent(button_equals);
		layout_advance.addComponent(button_equals);
	}
	
	public void layoutAdvance() {
		
		layout_advance.setBackground((gui, mc, x, y, width, height, mouseX, mouseY, windowActive) -> {
			Gui.drawRect(x, y, x + width, y + 20, Laptop.getSystem().getSettings().getColorScheme().getBackgroundColor());
			Gui.drawRect(x, y + 20, x + width, y + 21, Color.DARK_GRAY.getRGB());
			//mc.fontRenderer.drawString("CalculatorPlus", x + 22, y + 6, Color.WHITE.getRGB(), true);
		});
		
		textField_resultAdv = new TextField(2, 25, 192);
		textField_resultAdv.setEditable(false);
		layout_advance.addComponent(textField_resultAdv);
		
		button_square = new Button(110, 45, "^");
		button_square.setSize(16, 16);
		button_square.setToolTip("Square", "2^5");
		button_square.setClickListener((mouseX, mouseY, mouseButton) -> {
			if(mouseButton == 0) {
				writeText("^");
			}
		});
		layout_advance.addComponent(button_square);
		
		button_modulo = new Button(134, 45, "%");
		button_modulo.setSize(16, 16);
		button_modulo.setToolTip("Mod", "2%5");
		button_modulo.setClickListener((mouseX, mouseY, mouseButton) -> {
			if(mouseButton == 0) {
				writeText("%");
			}
		});
		layout_advance.addComponent(button_modulo);
		
		button_openBracket = new Button(155, 45, "(");
		button_openBracket.setSize(16, 16);
		button_openBracket.setToolTip("Open Bracket", "(34");
		button_openBracket.setClickListener((mouseX, mouseY, mouseButton) -> {
			if(mouseButton == 0) {
				writeText("(");
			}
		});
		layout_advance.addComponent(button_openBracket);
		
		button_closedBracket = new Button(179, 45, ")");
		button_closedBracket.setSize(16, 16);
		button_closedBracket.setToolTip("Closed Bracket", "56)");
		button_closedBracket.setClickListener((mouseX, mouseY, mouseButton) -> {
			if(mouseButton == 0) {
				writeText(")");
			}
		});
		layout_advance.addComponent(button_closedBracket);
		
		button_sqrt = new Button(110, 64, "sqrt()");
		button_sqrt.setSize(40, 16);
		button_sqrt.setToolTip("SquareRoot", "sqrt(54)");
		button_sqrt.setClickListener((mouseX, mouseY, mouseButton) -> {
			if(mouseButton == 0) {
				writeText("sqrt(");
			}
		});
		layout_advance.addComponent(button_sqrt);
		
		button_abs = new Button(110, 83, "abs()");
		button_abs.setSize(40, 16);
		button_abs.setToolTip("Absolute Value", "abs(-4)");
		button_abs.setClickListener((mouseX, mouseY, mouseButton) -> {
			if(mouseButton == 0) {
				writeText("abs(");
			}
		});
		layout_advance.addComponent(button_abs);
		
		button_sin = new Button(110, 102, "sin()");
		button_sin.setSize(40, 16);
		button_sin.setToolTip("Sin", "sin(34)");
		button_sin.setClickListener((mouseX, mouseY, mouseButton) -> {
			if(mouseButton == 0) {
				writeText("sin(");
			}
		});
		layout_advance.addComponent(button_sin);
		
		button_cos = new Button(155, 102, "cos()");
		button_cos.setSize(40, 16);
		button_cos.setToolTip("Cos", "cos(34)");
		button_cos.setClickListener((mouseX, mouseY, mouseButton) -> {
			if(mouseButton == 0) {
				writeText("cos(");
			}
		});
		layout_advance.addComponent(button_cos);
		
		button_tan = new Button(155, 83, "tan()");
		button_tan.setSize(40, 16);
		button_tan.setToolTip("Tan", "tan(34)");
		button_tan.setClickListener((mouseX, mouseY, mouseButton) -> {
			if(mouseButton == 0) {
				writeText("tan(");
			}
		});
		layout_advance.addComponent(button_tan);
		
		button_log = new Button(155, 64, "log()");
		button_log.setSize(40, 16);
		button_log.setToolTip("Log", "log(65)");
		button_log.setClickListener((mouseX, mouseY, mouseButton) -> {
			if(mouseButton == 0) {
				writeText("log(");
			}
		});
		layout_advance.addComponent(button_log);
	
	}
	
	
	public void layoutInfo() {
		
		layout_info.setBackground((gui, mc, x, y, width, height, mouseX, mouseY, windowActive) -> {
			Gui.drawRect(x, y, x + width, y + 20, Laptop.getSystem().getSettings().getColorScheme().getBackgroundColor());
			Gui.drawRect(x, y + 20, x + width, y + 21, Color.DARK_GRAY.getRGB());
			mc.fontRenderer.drawString("Info :", x + 22, y + 6, Laptop.getSystem().getSettings().getColorScheme().getTextColor(), true);
			mc.fontRenderer.drawString("CalculatorPlus Verison " + Reference.MOD_VERSION, x + 53, y + 6, Laptop.getSystem().getSettings().getColorScheme().getTextColor(), true);
			
			Gui.drawRect(x, y + 54, x + width, y + 46, Laptop.getSystem().getSettings().getColorScheme().getBackgroundColor());
			//Gui.drawRect(x, y + 55, x + width, y + 56, Color.DARK_GRAY.getRGB());
		});
		
		button_back = new Button(2, 2, Icons.ARROW_LEFT);
		button_back.setClickListener((mouseX, mouseY, mouseButton) -> {
			if(mode.equals("a")) {
				this.setCurrentLayout(layout_advance);
			}else if(mode.equals("h")){
				this.setCurrentLayout(layout_history);
			}else {
				this.setCurrentLayout(layout_normal);
			}
		}); 
		layout_info.addComponent(button_back);
		
		label_author = new Label("Author: TyrellPlayz", 2, 23);
		layout_info.addComponent(label_author);
		
		label_twitter = new Label("Twitter: RealTyrellPlayz", 2, 36);
		layout_info.addComponent(label_twitter);
		
		text_exp4j = new Text("This app uses a library not created by me called 'exp4j' to calculate. For more info about it: https://lallafa.objecthunter.net/exp4j/", 2, 56, 230);
		text_exp4j.setShadow(true);
		layout_info.addComponent(text_exp4j);
	}
	
	public void layoutHistory() {
		
		layout_history.setBackground((gui, mc, x, y, width, height, mouseX, mouseY, windowActive) -> {
			Gui.drawRect(x, y, x + width, y + 20, Laptop.getSystem().getSettings().getColorScheme().getBackgroundColor());
			Gui.drawRect(x, y + 20, x + width, y + 21, Color.DARK_GRAY.getRGB());
			//mc.fontRenderer.drawString("CalculatorPlus", x + 22, y + 6, Color.WHITE.getRGB(), true);
		});
		
		button_clearHistory = new Button(103, 2, Icons.CROSS);
		button_clearHistory.setToolTip("Clear History", "Clear all calculation history");
		button_clearHistory.setClickListener((mouseX, mouseY, mouseButton) -> {
			history.clear();
			refreshHistory();
		});
		layout_history.addComponent(button_clearHistory);
		
		button_refreshHistory = new Button(123, 2, Icons.RELOAD);
		button_refreshHistory.setToolTip("Refresh history", "Refreah all calculation history");
		button_refreshHistory.setClickListener((mouseX, mouseY, mouseButton) -> {
			refreshHistory();
		});
		layout_history.addComponent(button_refreshHistory);
		
		button_saveHistory = new Button(143, 2, Icons.SAVE);
		button_saveHistory.setToolTip("Save history", "Save your calculation history");
		button_saveHistory.setClickListener((mouseX, mouseY, mouseButton) -> {
			NBTTagCompound data = new NBTTagCompound();
			data.setString("history", history.toString());
            Dialog.SaveFile dialog = new Dialog.SaveFile(CalculatorApp.this, data);
            //dialog.setFolder(FileSystem.DIR_ROOT);
            openDialog(dialog);
		});
		layout_history.addComponent(button_saveHistory);
		
		button_loadHistory = new Button(163, 2, Icons.LOAD);
		button_loadHistory.setToolTip("Load history", "Load your calculation history");
		button_loadHistory.setClickListener((mouseX, mouseY, mouseButton) -> {
			Dialog.OpenFile dialog = new Dialog.OpenFile(this);
			dialog.setResponseHandler((success, file) ->{
				if(file.isForApplication(this)){
					String historyString = file.getData().getString("history");
					historyString = historyString.replace("[", "").replace("]", "");
					history = Lists.newArrayList(historyString.split(","));
					refreshHistory();
					return true;
				}
				else{
					Dialog.Message dialog2 = new Dialog.Message("Invalid file for CalculatorPlus");
					openDialog(dialog2);
				}
				return false;
			});
			openDialog(dialog);
		});
		layout_history.addComponent(button_loadHistory);
		
		textArea_history = new TextArea(2, 25, 192, 93);
		textArea_history.setEditable(false);
		refreshHistory();
		layout_history.addComponent(textArea_history);

	}

	/*
		Useful Methods
	 */

	public void addNumberClickListener(Button btn, final int number) {
		btn.setClickListener((mouseX, mouseY, mouseButton) ->{ 
			if(mouseButton == 0){
				if(textField_resultNor.getText().equals(result))
					textField_resultNor.clear();
				if(textField_resultNor.getText().equals("ERROR"))
					textField_resultNor.clear();
				if(!(textField_resultNor.getText().equals("0") && number == 0)){
					if(textField_resultNor.getText().equals("0"))
						textField_resultNor.clear();
					textField_resultNor.writeText(Integer.toString(number));
				}
				
				if(textField_resultAdv.getText().equals(result))
					textField_resultAdv.clear();
				if(textField_resultAdv.getText().equals("ERROR"))
					textField_resultAdv.clear();
				if(!(textField_resultAdv.getText().equals("0") && number == 0)){
					if(textField_resultAdv.getText().equals("0"))
						textField_resultAdv.clear();
					textField_resultAdv.writeText(Integer.toString(number));
				}
			}
        });
	}
	
	public void refreshHistory() {
		textArea_history.clear();
		if(history != null) {
			if(!history.equals(null)) {
				List<String> historyR = Lists.reverse(history);
				textArea_history.writeText("-> ");
				for(String his : historyR) {
					textArea_history.writeText(his);
					textArea_history.performReturn();
				}
			}
		}
	}

	/*
	Saving data
	 */

	public void writeText(String text) {
		if(textField_resultNor.getText().equals("ERROR") || textField_resultNor.getText().equals(result)) {
			textField_resultNor.clear();
		}
		if(textField_resultAdv.getText().equals("ERROR") || textField_resultAdv.getText().equals(result)) {
			textField_resultAdv.clear();
		}
		textField_resultNor.writeText(text);
		textField_resultAdv.writeText(text);
	}
	
	public void setText(String text, TextArea textarea) {
		textarea.clear();
		textarea.writeText(text);
	}

	@Override
	public void onTick() {
		refreshHistory();
		super.onTick();
	}

	@Override
	public void load(NBTTagCompound tagCompound) {
	}

	@Override
	public void save(NBTTagCompound tagCompound) {
	}
	
}
