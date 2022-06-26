package tk.starworldstudio;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import static org.bukkit.Bukkit.broadcastMessage;


public final class echocave extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logice
        getLogger().info("Enabled !");
        saveDefaultConfig();
        getLogger().info(""+ getCaves().toString().replaceAll("§","&"));
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled !");
    }
    public List getCaves(){
        FileConfiguration config = getConfig();
        return config.getList("caves");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("cave")) {
            if (args.length == 1) {
                String[] subCommands = {"add", "del","get","num","help"};
                return Arrays.asList(subCommands);
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("add")) {
                    return Arrays.asList("<Content>");
                }
                if (args[0].equalsIgnoreCase("del")) {
                    return Arrays.asList("<EchoCaveID>");
                }
                if (args[0].equalsIgnoreCase("get")) {
                    return Arrays.asList("<EchoCaveID>");
                }
                if (args[0].equalsIgnoreCase("help")) {
                    return Arrays.asList("");
                }
                if (args[0].equalsIgnoreCase("num")) {
                    return Arrays.asList("");
                }
            }
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("cave")) {
            //向玩家发送消息
            if (args.length == 0) {
                // 从getCaves()中随机取一个
                int random = (int) (Math.random() * getCaves().size());
                LinkedHashMap lmap = (LinkedHashMap) getCaves().get(random);
                sender.sendMessage("\n§f│ §b回声洞 —— ("+random+")\n§f│ \n§f│ §e"+lmap.get("content").toString().replace("|", "\n§f│ &e").replace("&","§")+"\n"+"§f│ §b—— "+lmap.get("player").toString());
                return true;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("num")) {
                    sender.sendMessage("§f│ §b回声洞 —— 回声洞数量（从0开始）："+(getCaves().size()-1));
                    return true;
                }
            }
            else{
                if (Objects.equals(args[0], "get")) {
                    LinkedHashMap lmap = (LinkedHashMap) getCaves().get(Integer.parseInt(args[1]));
                    sender.sendMessage("\n§f│ §b回声洞 —— ("+args[1]+")\n§f│ \n§f│ §e"+lmap.get("content").toString().replace("|", "\n§f│ &e").replace("&","§")+"\n"+"§f│ §b—— "+lmap.get("player").toString());
                    return true;
                }
                if (Objects.equals(args[0], "add")) {
                    List cave_content_list = Arrays.asList(Arrays.stream(args).toArray());
                    cave_content_list = cave_content_list.subList(1, args.length);
                    String cave_content = String.join(" ",cave_content_list);
                    LinkedHashMap lmap = new LinkedHashMap();
                    if (!(sender instanceof Player)) {
                        lmap.put("player", "[Server]");
                    }
                    else {
                        lmap.put("player", ((Player) sender).getDisplayName());
                    }
                    lmap.put("content", cave_content);
                    List Caves = getCaves();
                    Caves.add(lmap);
                    getConfig().set("caves", Caves);
                    saveConfig();
                    sender.sendMessage("§f│ §b回声洞投稿成功！");
                    int size = Caves.size()-1;
                    sender.sendMessage("§f│ §b使用/cave get "+size+" 查看");
                    broadcastMessage("§e新的回声洞! ("+size+")");
                    return true;
                }
                if (Objects.equals(args[0], "del")) {
                    //判断是否有op权限
                    if (sender.isOp()) {
                        List Caves = getCaves();
                        LinkedHashMap lmap = new LinkedHashMap();
                        lmap.put("content", "§c[数据删除]");
                        lmap.put("player", "[Error]");
                        Caves.set(Integer.parseInt(args[1]), lmap);
                        getConfig().set("caves", Caves);
                        saveConfig();
                        sender.sendMessage("§f│ §b回声洞删除成功！");
                        broadcastMessage("§e回声洞"+Integer.parseInt(args[1])+"被删除");

                    }else {
                        sender.sendMessage("§f│ §c你没有权限删除！");
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
