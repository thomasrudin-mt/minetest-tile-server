package io.rudin.minetest.tileserver.listener;

import com.google.common.eventbus.Subscribe;
import io.rudin.minetest.tileserver.MapBlock;
import io.rudin.minetest.tileserver.parser.Metadata;
import io.rudin.minetest.tileserver.service.EventBus;
import org.jooq.DSLContext;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;


public class FancyVendAdminBlockListener {


    private static final String BLOCK_NAME = "fancy_vend:admin_vendor";

    @Inject
    public FancyVendAdminBlockListener(EventBus eventBus, DSLContext ctx){
        this.eventBus = eventBus;
        this.ctx = ctx;
    }

    private final DSLContext ctx;

    private final EventBus eventBus;

    public void setup(){
        eventBus.register(this);
    }

    /*
    {
owner=admin,
configured=true,
settings=
 return {["admin_vendor"] = true, ["depositor"] = false, ["split_incoming_stacks"] = false,
 ["output_item"] = "currency:minegeld", ["quit"] = true, ["accept_worn_input"] = true, ["digiline_channel"] = "", ["output_item_qty"] = 1,
 ["auto_sort"] = false, ["input_item"] = "default:coal_lump", ["currency_eject"] = false, ["accept_output_only"] = false, ["co_sellers"] = "",
 ["accept_worn_output"] = true, ["banned_buyers"] = "", ["input_item_qty"] = 1, ["inactive_force"] = false},
item=currency:minegeld,
infotext=Admin Vendor trading 1 Coal Lump for 1 1 MineGeld Note (owned by admin),
log=return {"Vendor placed by admin", "Player ilai purchased 1 lots from this vendor.", "Player ilai purchased 1 lots from this vendor.", "Player ilai purchased 1 lots from this vendor.", "Player ilai purchased 1 lots from this vendor.", "Player ilai purchased 1 lots from this vendor.", "Player ilai purchased 1 lots from this vendor.", "Player ilai purchased 1 lots from this vendor.", "Player ilai purchased 1 lots from this vendor.", "Player ilai purchased 1 lots from this vendor.", "Player ilai purchased 1 lots from this vendor.", "Player ilai purchased 1 lots from this vendor.", "Player ilai purchased 1 lots from this vendor.", "Player ilai purchased 1 lots from this vendor.", "Player ilai purchased 1 lots from this vendor.", "Player ilai purchased 1 lots from this vendor.", "Player ilai purchased 1 lots from this vendor.", "Player didi1 purchased 1 lots from this vendor.", "Player didi1 purchased 1 lots from this vendor.", "Player LalTilism purchased 1 lots from this vendor."},
message=You have insufficient funds
}

     */

    private void register(MapBlock mapBlock, int x, int y, int z){
        Metadata metadata = mapBlock.getMetadata();

        int position = MapBlock.toPosition(x, y, z);

        Map<String, String> map = metadata.map.get(position);

        int posx = (mapBlock.x * 16) + x;
        int posy = (mapBlock.y * 16) + y;
        int posz = (mapBlock.z * 16) + z;

        System.out.println(map);//XXX

    }

    @Subscribe
    public void onMapBlock(EventBus.MapBlockParsedEvent event){

        MapBlock mapblock = event.mapblock;

        if (mapblock.mapping.containsValue(BLOCK_NAME)){

            for (int x=0; x<16; x++){
                for (int y=0; y<16; y++){
                    for (int z=0; z<16; z++){
                        Optional<String> node = mapblock.getNode(x, y, z);

                        if (node.isPresent() && node.get().equals(BLOCK_NAME)){
                            register(mapblock, x,y,z);
                        }
                    }
                }
            }

        }

    }
}
