package util;

import java.awt.Component;
import java.awt.Window;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public final class DialogUtil {

    private DialogUtil() {
    }

    private static Component owner(Component parent) {
        if (parent == null) {
            return null;
        }
        if (parent instanceof Window) {
            return parent;
        }
        Window window = SwingUtilities.getWindowAncestor(parent);
        return window != null ? window : parent;
    }

    public static void showMessageDialog(Component parent, Object message) {
        JOptionPane.showMessageDialog(owner(parent), message);
    }

    public static void showMessageDialog(Component parent, Object message, String title, int messageType) {
        JOptionPane.showMessageDialog(owner(parent), message, title, messageType);
    }

    public static void showMessageDialog(Component parent, Object message, String title, int messageType, Icon icon) {
        JOptionPane.showMessageDialog(owner(parent), message, title, messageType, icon);
    }

    public static int showConfirmDialog(Component parent, Object message, String title, int optionType) {
        return JOptionPane.showConfirmDialog(owner(parent), message, title, optionType);
    }

    public static int showConfirmDialog(Component parent, Object message, String title, int optionType, int messageType) {
        return JOptionPane.showConfirmDialog(owner(parent), message, title, optionType, messageType);
    }

    public static String showInputDialog(Component parent, Object message) {
        return JOptionPane.showInputDialog(owner(parent), message);
    }

    public static String showInputDialog(Component parent, Object message, String title, int messageType) {
        return JOptionPane.showInputDialog(owner(parent), message, title, messageType);
    }

    public static Object showInputDialog(Component parent, Object message, String title, int messageType, Icon icon,
            Object[] selectionValues, Object initialSelectionValue) {
        return JOptionPane.showInputDialog(owner(parent), message, title, messageType, icon, selectionValues, initialSelectionValue);
    }

    public static int showOptionDialog(Component parent, Object message, String title, int optionType, int messageType, Icon icon,
            Object[] options, Object initialValue) {
        return JOptionPane.showOptionDialog(owner(parent), message, title, optionType, messageType, icon, options, initialValue);
    }
}
