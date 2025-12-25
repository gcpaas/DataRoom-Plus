import { ChartDefine } from '@/packages/components/type/define.ts'
import thumbnail from './images/text.png'

export default class DrTextPlugin extends ChartDefine {
  constructor(tags: string[]) {
    super('DrText', '文本', thumbnail, tags)
  }
}
