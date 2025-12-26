import { ChartPlugin } from '@/packages/components/type/define.ts'
import thumbnail from './images/text.png'

export default class DrTextPlugin extends ChartPlugin {
  constructor(tags: string[]) {
    super('DrText', '文本', thumbnail, tags)
  }
}
