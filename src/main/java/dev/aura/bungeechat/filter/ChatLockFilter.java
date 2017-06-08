package dev.aura.bungeechat.filter;

import java.util.LinkedList;
import java.util.List;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.filter.BlockMessageException;
import dev.aura.bungeechat.api.filter.BungeeChatFilter;
import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.permission.PermissionManager;

public class ChatLockFilter implements BungeeChatFilter {
    private boolean globalLock = false;
    private List<String> lockedServers = new LinkedList<>();

    @Override
    public String applyFilter(BungeeChatAccount sender, String message) throws BlockMessageException {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_CHAT_LOCK_BYPASS)
                || !((globalLock && MessagesService.getGlobalPredicate().test(sender))
                        || lockedServers.contains(sender.getServerName())))
            return message;
        else
            throw new BlockMessageException(Message.CHAT_IS_DISABLED.get(sender, message));
    }

    @Override
    public int getPriority() {
        return FilterManager.LOCK_CHAT_FILTER_PRIORITY;
    }

    public void enableGlobalChatLock() {
        globalLock = true;
    }

    public void enableLocalChatLock(String name) {
        lockedServers.add(name);
    }

    public boolean isGlobalChatLockEnabled() {
        return globalLock;
    }

    public boolean isLocalChatLockEnabled(String name) {
        return lockedServers.contains(name);
    }

    public void disableGlobalChatLock() {
        globalLock = false;
    }

    public void disableLocalChatLock(String name) {
        lockedServers.remove(name);
    }
}
