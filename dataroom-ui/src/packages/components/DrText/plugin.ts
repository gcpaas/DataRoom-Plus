import { ChartPlugin } from '@/packages/components/type/define.ts'
import thumbnail from './images/text.png'
import { getParentFolderName } from '@/packages/_common/_utils.ts'
import { DrConst } from '@/packages/_common/_constant.ts'

export const pluginType = getParentFolderName(import.meta.url)

export class DrTextPlugin extends ChartPlugin {
  constructor(tags: string[]) {
    super(DrConst.THIS_PLUGIN_TYPE, '文本', '文字,文本,数字', thumbnail, tags)
  }
}
